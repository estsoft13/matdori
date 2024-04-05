package com.estsoft13.matdori.repository;

import com.estsoft13.matdori.domain.Restaurant;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RestaurantRepositoryTest {

    @Autowired
    RestaurantRepository restaurantRepository;

    @BeforeEach
    public void setup() {
        restaurantRepository.deleteAll();
    }

    @AfterEach
    public void cleanup() {
        restaurantRepository.deleteAll();
    }

    // save, findAll 테스트
    @Test
    public void getRestaurants_test() {
        // given
        String name = "맛도리";
        String address = "서울시 강남구";
        String category = "한식";

        restaurantRepository.save(new Restaurant(name, address, category));

        // when
        List<Restaurant> restaurantList = restaurantRepository.findAll();

        // then
        Restaurant restaurant = restaurantList.get(0);
        assertEquals(restaurant.getName(), name);
        assertEquals(restaurant.getAddress(), address);
        assertEquals(restaurant.getCategory(), category);
    }

    // delete 테스트
    @Test
    public void deleteRestaurant_test() {
        // given
        String name = "맛도리";
        String address = "서울시 강남구";
        String category = "한식";

        Restaurant restaurant = new Restaurant(name, address, category);
        restaurantRepository.save(restaurant);

        // when
        restaurantRepository.deleteById(restaurant.getId());

        // then
        assertEquals(restaurantRepository.findAll().size(), 0);

    }

    @Test
    public void updateRestaurant_test() {
        // given
        String name = "맛도리";
        String address = "서울시 강남구";
        String category = "한식";

        Restaurant savedRestaurant = restaurantRepository.save(new Restaurant(name, address, category));

        // when
        Long savedRestaurantId = savedRestaurant.getId(); // 저장된 식당의 ID를 가져옴
        Restaurant restaurant = restaurantRepository.findById(savedRestaurantId)
                .orElseThrow(() -> new IllegalArgumentException("식당 id를 찾을 수 없습니다."));
        restaurant.update("맛도리2", "서울시 강북구", "중식");

        // then
        assertEquals(restaurant.getName(), "맛도리2");
        assertEquals(restaurant.getAddress(), "서울시 강북구");
        assertEquals(restaurant.getCategory(), "중식");

    }
}