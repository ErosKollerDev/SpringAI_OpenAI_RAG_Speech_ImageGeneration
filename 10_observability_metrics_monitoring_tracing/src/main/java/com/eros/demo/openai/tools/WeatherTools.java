package com.eros.demo.openai.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;


@Component
public class WeatherTools {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherTools.class);

    @Tool(name="getWeather", description = "Get the current weather in your location", returnDirect = true)
    String getCurrentWeather() {
        LOGGER.info("Returning the  current weather ");
//        throw new RuntimeException("Weather API not available ... pls try again later");
        return "Uhhhh, let me look out side, gray, cloudy outside, 21 degrees, not raining today ";
    }

}
