package com.thoughtworks.parking_lot.controller;

import com.thoughtworks.parking_lot.model.Order;
import com.thoughtworks.parking_lot.model.ParkingLot;
import com.thoughtworks.parking_lot.service.OrderService;
import com.thoughtworks.parking_lot.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parkingLot/{parkingLotName}/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    ParkingLotService parkingLotService;


    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<Order> addParkingOrder(@PathVariable String parkingLotName, @RequestBody Order order){
        ParkingLot parkingLot =  parkingLotService.getSpecificParkingLot(parkingLotName);
        parkingLotService.deductParkingLotCapacity(parkingLotName);
        return orderService.addParkingOrder(parkingLot, order);
    }

}
