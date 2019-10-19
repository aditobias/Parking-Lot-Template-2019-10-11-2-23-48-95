package com.thoughtworks.parking_lot.service;

import com.thoughtworks.parking_lot.model.ParkingLot;
import com.thoughtworks.parking_lot.repository.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingLotService {

    @Autowired
    ParkingLotRepository parkingLotRepository;

    public ParkingLot addParkingLot(ParkingLot newParkingLot) {
        parkingLotRepository.save(newParkingLot);
        return newParkingLot;
    }

    public boolean deleteParkingLot(String parkingLotName) {
        ParkingLot parkingLotForDeletion = parkingLotRepository.findByName(parkingLotName);

        if(parkingLotForDeletion != null){
            parkingLotRepository.delete(parkingLotForDeletion);
            return true;
        }

        return false;
    }
}
