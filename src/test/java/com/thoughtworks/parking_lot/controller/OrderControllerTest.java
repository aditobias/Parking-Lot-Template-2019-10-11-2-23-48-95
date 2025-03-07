package com.thoughtworks.parking_lot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.parking_lot.model.ParkingLot;
import com.thoughtworks.parking_lot.model.ParkingOrder;
import com.thoughtworks.parking_lot.service.ParkingLotService;
import com.thoughtworks.parking_lot.service.ParkingOrderService;
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

import java.sql.Timestamp;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderController.class)
@ActiveProfiles(profiles = "test")
class OrderControllerTest {

    @MockBean
    ParkingLotService parkingLotService;

    @MockBean
    ParkingOrderService orderService;

    @Autowired
    private MockMvc mvc;

    private ParkingLot parkingLot = new ParkingLot();
    private ParkingOrder order = new ParkingOrder();

    @Test
    public void should_return_new_parking_order_when_given_valid_parking_lot_name_and_plate_number() throws Exception {
        buildParkingLot();
        buildParkingLotOrder();

        ParkingOrder newOrder = new ParkingOrder();
        newOrder.setPlateNumber("PLATE");

        when(parkingLotService.getSpecificParkingLot("OOCL")).thenReturn(parkingLot);
        doNothing().when(parkingLotService).deductParkingLotCapacity(anyString());
        when(orderService.addParkingOrder(any(), any())).thenReturn(new ResponseEntity<>(order, HttpStatus.OK));

        ResultActions result = mvc.perform(post("/parkingLot/{parkingLotName}/order", "OOCL")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(newOrder)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.number", is("123ABC")))
                .andExpect(jsonPath("$.plateNumber", is("PLATE")))
                .andExpect(jsonPath("$.parkingLotName", is("Test")));

    }

    @Test
    public void should_return_parking_order_when_given_valid_parking_name_and_parking_order() throws Exception {
        buildParkingLot();
        buildParkingLotOrder();


        when(parkingLotService.getSpecificParkingLot("OOCL")).thenReturn(parkingLot);
        doNothing().when(parkingLotService).increaseParkingLotCapacity(anyString());
        order.setOrderStatus("CLOSED");
        order.setOutTime(Timestamp.valueOf("2019-10-22 10:00:00.0"));
        when(orderService.updateParkingOrder("TESTING")).thenReturn(new ResponseEntity<>(order, HttpStatus.OK));
        ResultActions result = mvc.perform(patch("/parkingLot/{parkingLotName}/order/{orderNumber}"
                , "OOCL", "TESTING")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(order)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus", is("CLOSED")))
                .andExpect(jsonPath("$.outTime", is("2019-10-22T02:00:00.000+0000")));

    }

    private void buildParkingLotOrder() {
        order.setNumber("123ABC");
        order.setPlateNumber("PLATE");
        order.setParkingLotName(parkingLot.getName());
        order.setInTime(Timestamp.valueOf("2019-10-21 10:00:00.0"));
        order.setOutTime(null);
        order.setOrderStatus("open");
    }

    private void buildParkingLot() {
        parkingLot.setCapacity(10);
        parkingLot.setName("Test");
        parkingLot.setLocation("Parañaque");
    }

    static String asJsonString(final ParkingOrder obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}