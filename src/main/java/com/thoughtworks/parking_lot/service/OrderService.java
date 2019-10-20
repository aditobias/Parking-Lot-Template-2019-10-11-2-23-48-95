package com.thoughtworks.parking_lot.service;

import com.thoughtworks.parking_lot.model.Order;
import com.thoughtworks.parking_lot.model.ParkingLot;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class OrderService {


    public ResponseEntity<Order> addParkingOrder(ParkingLot parkingLot, Order order) {
        order.setInTime(new Timestamp(System.currentTimeMillis()));
        order.setParkingLotName(parkingLot.getName());
        order.setNumber(generateRandomString());

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    public String generateRandomString(){
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}
