package com.thoughtworks.parking_lot.service;

import com.thoughtworks.parking_lot.model.ParkingLot;
import com.thoughtworks.parking_lot.repository.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public ParkingLot getSpecificParkingLot(String parkingLotName) {
        ParkingLot parkingLot = parkingLotRepository.findByName(parkingLotName);

        return parkingLot;
    }

    public Iterable<ParkingLot> getParkingLotPageAndPageSize(Integer page, Integer pageSize) {
       return parkingLotRepository.findAll(PageRequest.of(page, pageSize));

    }

    public ResponseEntity<ParkingLot> updateParkingLot(String parkingLotName, ParkingLot updatedParkingLot) {
        ParkingLot oldParkingLot = parkingLotRepository.findByName(parkingLotName);

        if(oldParkingLot != null){
            oldParkingLot.setCapacity(updatedParkingLot.getCapacity());
            parkingLotRepository.save(oldParkingLot);
            return new ResponseEntity<>(oldParkingLot, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public void deductParkingLotCapacity(String parkingLotName){
        ParkingLot oldParkingLot = parkingLotRepository.findByName(parkingLotName);

        if(oldParkingLot != null){
            oldParkingLot.setCapacity(oldParkingLot.getCapacity() - 1);
            parkingLotRepository.save(oldParkingLot);
        }
    }

    public void increaseParkingLotCapacity(String parkingLotName) {
        ParkingLot oldParkingLot = parkingLotRepository.findByName(parkingLotName);

        if(oldParkingLot != null){
            oldParkingLot.setCapacity(oldParkingLot.getCapacity() + 1);
            parkingLotRepository.save(oldParkingLot);
        }
    }
}
