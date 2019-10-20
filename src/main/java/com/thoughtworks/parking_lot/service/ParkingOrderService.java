package com.thoughtworks.parking_lot.service;

import com.thoughtworks.parking_lot.model.ParkingLot;
import com.thoughtworks.parking_lot.model.ParkingOrder;
import com.thoughtworks.parking_lot.repository.ParkingOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class ParkingOrderService {

    @Autowired
    ParkingOrderRepository orderRepository;

    public ResponseEntity<ParkingOrder> addParkingOrder(ParkingLot parkingLot, ParkingOrder parkingOrder) {
        parkingOrder.setInTime(new Timestamp(System.currentTimeMillis()));
        parkingOrder.setParkingLotName(parkingLot.getName());
        parkingOrder.setNumber(generateRandomString());
        parkingOrder.setOrderStatus("OPEN");

        orderRepository.save(parkingOrder);

        return new ResponseEntity<>(parkingOrder, HttpStatus.OK);
    }

    public String generateRandomString(){
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

    public ResponseEntity<ParkingOrder> updateParkingOrder(String orderNumber) {
        ParkingOrder customerOrder = orderRepository.findByNumber(orderNumber);

        if(customerOrder != null){
            customerOrder.setOrderStatus("CLOSED");
            customerOrder.setOutTime(new Timestamp(System.currentTimeMillis()));
            orderRepository.save(customerOrder);

            return new ResponseEntity<>(customerOrder, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
