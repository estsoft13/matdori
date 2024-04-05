package com.estsoft13.matdori.controller.restaurant;

import com.estsoft13.matdori.domain.Restaurant;
import com.estsoft13.matdori.dto.restaurant.UpdateRestRequestDto;
import com.estsoft13.matdori.dto.restaurant.UpdateRestResponseDto;
import com.estsoft13.matdori.repository.RestaurantRepository;
import com.estsoft13.matdori.service.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
public class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantService restaurantService;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    public void deleteAll() {
        restaurantRepository.deleteAll();
    }

    @Test
    public void getRestaurantsTest() throws Exception {
        // given
        String url = "/api/restaurants";
        Restaurant restaurant = restaurantRepository.save(new Restaurant("맛집1", "서울시 강남구", "한식"));
        Restaurant restaurant2 = restaurantRepository.save(new Restaurant("맛집2", "서울시 강남구", "한식"));

        // when
        ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(restaurant.getId()))
                .andExpect(jsonPath("$[0].name").value(restaurant.getName()))
                .andExpect(jsonPath("$[0].address").value(restaurant.getAddress()))
                .andExpect(jsonPath("$[0].category").value(restaurant.getCategory()))
                .andExpect(jsonPath("$[1].id").value(restaurant2.getId()))
                .andExpect(jsonPath("$[1].name").value(restaurant2.getName()))
                .andExpect(jsonPath("$[1].address").value(restaurant2.getAddress()))
                .andExpect(jsonPath("$[1].category").value(restaurant2.getCategory()));
    }

    @Test
    public void getRestaurantTest() throws Exception {
        // given
        Restaurant restaurant = restaurantRepository.save(new Restaurant("맛집1", "서울시 강남구", "한식"));
        String url = "/api/restaurant/" + restaurant.getId();

        // when
        ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(restaurant.getId()))
                .andExpect(jsonPath("$.name").value(restaurant.getName()))
                .andExpect(jsonPath("$.address").value(restaurant.getAddress()))
                .andExpect(jsonPath("$.category").value(restaurant.getCategory()));
    }

    @Test
    public void deleteRestaurantTest() throws Exception {
        // given
        Restaurant restaurant = restaurantRepository.save(new Restaurant("맛집1", "서울시 강남구", "한식"));
        String url = "/api/restaurant/" + restaurant.getId();

        // when
        ResultActions result = mockMvc.perform(delete(url).accept(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk());
    }

    @Test
    public void updateRestaurantTest() throws Exception {
        // given
        Restaurant restaurant = restaurantRepository.save(new Restaurant("맛집1", "서울시 강남구", "한식"));
        Long id = restaurant.getId();
        UpdateRestRequestDto requestDto = new UpdateRestRequestDto("맛집2", "부산", "중식");
        UpdateRestResponseDto responseDto = restaurantService.updateRestaurant(restaurant.getId(), requestDto);

        //when
        ResultActions result = mockMvc.perform(put("/api/restaurant/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto))
                .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(responseDto.getName()))
                .andExpect(jsonPath("$.address").value(responseDto.getAddress()))
                .andExpect(jsonPath("$.category").value(responseDto.getCategory()));
    }
}
