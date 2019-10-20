package com.thoughtworks.parking_lot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.parking_lot.model.ParkingLot;
import com.thoughtworks.parking_lot.service.ParkingLotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private ParkingLot parkingLot = new ParkingLot();

    @Test

    void should_return_posted_parkingLot_when_given_new_parking_lot() throws Exception {
        buildParkingLot();

        when(parkingLotService.addParkingLot(any())).thenReturn(parkingLot);

        ResultActions result = mvc.perform(post("/parkingLot")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(parkingLot)));

        result.andExpect(status().isCreated()).andExpect(jsonPath("$.name", is("Test")));
    }

    @Test
    void should_return_OK_response_when_given_valid_parking_lot_name() throws Exception {
        buildParkingLot();

        when(parkingLotService.deleteParkingLot("OOCLPARK")).thenReturn(true);

        ResultActions result = mvc.perform(delete("/parkingLot/{parkingLotName}", "OOCLPARK"));

        result.andExpect(status().isOk());
    }

    @Test
    void should_return_NOT_FOUND_response_when_given_invalid_parking_lot_name() throws Exception {
        buildParkingLot();

        ResultActions result = mvc.perform(delete("/parkingLot/{parkingLotName}", "OOCLPARK"));

        result.andExpect(status().isNotFound());
    }

    @Test
    void should_return_parkingLot_and_OK_response_when_given_valid_parking_lot_name() throws Exception {
        buildParkingLot();

        when(parkingLotService.getSpecificParkingLot("OOCLPARKING"))
                .thenReturn(new ResponseEntity<>(parkingLot, HttpStatus.OK));

        ResultActions result = mvc.perform(get("/parkingLot/{parkingLotName}", "OOCLPARKING"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test")))
                .andExpect(jsonPath("$.capacity", is(10)))
                .andExpect(jsonPath("$.location",is("Parañaque")));
    }

    @Test
    void should_return_NOT_FOUND_when_given_invalid_parking_lot_name() throws Exception {
        buildParkingLot();

        when(parkingLotService.getSpecificParkingLot("INVALID"))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResultActions result = mvc.perform(get("/parkingLot/{parkingLotName}", "INVALID"));

        result.andExpect(status().isNotFound());
    }

    @Test
    void should_return_list_of_parking_lot_when_given_indicated_page_and_page_size() throws Exception {
        buildParkingLot();

        when(parkingLotService.getParkingLotPageAndPageSize(0,15))
                .thenReturn(Collections.singletonList(parkingLot));

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();

        requestParams.add("page", "0");
        requestParams.add("pageSize", "15");

        ResultActions result = mvc.perform(get("/parkingLot").params(requestParams));

        result.andExpect(status().isOk()).andExpect(jsonPath("$[0].name", is("Test")));
    }

    @Test
    void should_return_updated_parking_lot_when_given_valid_parking_lot_and_parking_lot() throws Exception {
        buildParkingLot();

        ParkingLot updatedParkingLot = new ParkingLot();
        updatedParkingLot.setName(parkingLot.getName());
        updatedParkingLot.setLocation(parkingLot.getLocation());
        updatedParkingLot.setCapacity(20);

        when(parkingLotService.updateParkingLot(eq("OOCLPARKING"),any()))
                .thenReturn(new ResponseEntity<>(updatedParkingLot, HttpStatus.OK));

        ResultActions result = mvc.perform(patch("/parkingLot/{parkingLotName}", "OOCLPARKING")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(parkingLot)));



        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.capacity", is(20)));
    }

    @Test
    void should_return_NOT_FOUND_when_given_invalid_parking_lot_and_parking_lot() throws Exception {
        buildParkingLot();

        when(parkingLotService.updateParkingLot(eq("INVALID"),any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResultActions result = mvc.perform(patch("/parkingLot/{parkingLotName}", "INVALID")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(parkingLot)));

        result.andExpect(status().isNotFound());
    }

    private void buildParkingLot() {
        parkingLot.setCapacity(10);
        parkingLot.setName("Test");
        parkingLot.setLocation("Parañaque");
    }

    static String asJsonString(final ParkingLot obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}