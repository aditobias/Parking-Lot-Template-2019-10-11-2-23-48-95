package com.thoughtworks.parking_lot.controller;

import com.thoughtworks.parking_lot.model.ParkingLot;
import com.thoughtworks.parking_lot.model.ParkingOrder;
import com.thoughtworks.parking_lot.service.ParkingLotService;
import com.thoughtworks.parking_lot.service.ParkingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parkingLot/{parkingLotName}/order")
public class OrderController {

    @Autowired
    ParkingOrderService parkingOrderService;

    @Autowired
    ParkingLotService parkingLotService;


    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<ParkingOrder> addParkingOrder(@PathVariable String parkingLotName,
                                                        @RequestBody ParkingOrder parkingOrder){
        ParkingLot parkingLot =  parkingLotService.getSpecificParkingLot(parkingLotName);
        parkingLotService.deductParkingLotCapacity(parkingLotName);
        return parkingOrderService.addParkingOrder(parkingLot, parkingOrder);
    }

    @PatchMapping(path = "/{orderNumber}", consumes = {"application/json"})
    public ResponseEntity<ParkingOrder> updateParkingOrder(@PathVariable String parkingLotName,
                                                           @PathVariable String orderNumber){
        parkingLotService.increaseParkingLotCapacity(parkingLotName);
        return parkingOrderService.updateParkingOrder(orderNumber);
    }
}
