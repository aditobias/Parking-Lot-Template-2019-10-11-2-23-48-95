package com.thoughtworks.parking_lot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.parking_lot.model.ParkingLot;
import com.thoughtworks.parking_lot.service.ParkingLotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ParkingLotController.class)
@ActiveProfiles(profiles = "test")
public class ParkingLotControllerTest {

    @MockBean
    ParkingLotService parkingLotService;

    @Autowired
    private MockMvc mvc;

    ParkingLot parkingLot = new ParkingLot();

    @Test
    public void should_return_posted_parkingLot_when_given_new_parking_lot() throws Exception {
        buildParkingLot();

        when(parkingLotService.addParkingLot(any())).thenReturn(parkingLot);

        ResultActions result = mvc.perform(post("/parkingLot")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(parkingLot)));

        result.andExpect(status().isCreated()).andExpect(jsonPath("$.name", is("Test")));
    }

    private void buildParkingLot() {
        parkingLot.setCapacity(10);
        parkingLot.setName("Test");
        parkingLot.setLocation("Parañaque");
    }

    public static String asJsonString(final ParkingLot obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}