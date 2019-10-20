package com.thoughtworks.parking_lot.service;

import com.thoughtworks.parking_lot.model.ParkingLot;
import com.thoughtworks.parking_lot.repository.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<ParkingLot> getSpecificParkingLot(String parkingLotName) {
        ParkingLot parkingLot = parkingLotRepository.findByName(parkingLotName);

        if(parkingLot != null){
            return new ResponseEntity<>(parkingLot, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public Iterable<ParkingLot> getParkingLotPageAndPageSize(Integer page, Integer pageSize) {
       return parkingLotRepository.findAll(PageRequest.of(page, pageSize));

    }
}
