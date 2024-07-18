package com.example.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.entity.vo.response.WeatherVO;
import com.example.service.WeatherService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Resource
    RestTemplate rest;
    @Value("${spring.weather.key}")
    String key;
    @Resource
    StringRedisTemplate template;

    @Override
    public WeatherVO fetchWeather(double longitude, double latitude) {
        return fetchFromCache(longitude, latitude);
    }


    /**
     * 获取天气的具体信息(先从缓存中取，没有的话就去调和风天气的接口)
     * @param longitude 经度
     * @param latitude 纬度
     * @return 天气VO
     */
    private WeatherVO fetchFromCache(double longitude, double latitude){
        byte[] data = rest.getForObject(
                "https://geoapi.qweather.com/v2/city/lookup?location="+longitude+","+latitude+"&key="+key, byte[].class);
        JSONObject geo = decompressStringTOJson(data);
        if(geo == null) return null;
        JSONObject location = geo.getJSONArray("location").getJSONObject(0);
        String id = location.getString("id");
        String key = Const.FORUM_WERTHER_CACHE+id;
        String cache = template.opsForValue().get(key);
        if(cache != null)
            return JSONObject.parseObject(cache).to(WeatherVO.class);
        WeatherVO vo = this.fetchFromApi(id, location);
        if(vo == null) return null;
        template.opsForValue().set(key, JSONObject.from(vo).toJSONString(),1, TimeUnit.HOURS);
        return vo;
    }

    private WeatherVO fetchFromApi(String id, JSONObject location){
        WeatherVO vo = new WeatherVO();
        vo.setLocation(location);
        byte[] data = rest.getForObject(
                "https://devapi.qweather.com/v7/weather/now?location="+id+"&key="+key, byte[].class);
        JSONObject now = decompressStringTOJson(data);
        if(now == null) return null;
        vo.setNow(now.getJSONObject("now"));
        byte[] data1 = rest.getForObject(
                "https://devapi.qweather.com/v7/weather/24h?location="+id+"&key="+key, byte[].class);
        JSONObject hourly = decompressStringTOJson(data1);
        vo.setHourly(new JSONArray(hourly.getJSONArray("hourly").stream().limit(5).toList()));
        return vo;
    }

    private JSONObject decompressStringTOJson(byte[] data){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try{
            GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(data));
            byte[] buffer = new byte[1024];
            int read;
            while ((read = gzip.read(buffer)) != -1)
                stream.write(buffer, 0, read);
            gzip.close();
            stream.close();

            return JSONObject.parseObject(stream.toString());
        }catch (IOException e){
            return null;
        }

    }
}
