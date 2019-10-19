package com.thoughtworks.parking_lot.controller;


import com.thoughtworks.parking_lot.model.ParkingLot;
import com.thoughtworks.parking_lot.service.ParkingLotService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parkingLot")
public class ParkingLotController {

    @Autowired
    ParkingLotService parkingLotService;

    @PostMapping(produces = {"application/json"}, consumes = {"application/json"})
    @ResponseStatus(code = HttpStatus.CREATED)
    public ParkingLot addParkingLot(@RequestBody ParkingLot newParkingLot){
        return parkingLotService.addParkingLot(newParkingLot);
    }

    @DeleteMapping(value = "/{parkingLotName}")
    public ResponseEntity<ParkingLot> deleteParkingLot(@PathVariable String parkingLotName){
        if(parkingLotService.deleteParkingLot(parkingLotName)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{parkingLotName}", produces = {"application/json"})
    public ResponseEntity<ParkingLot> getSpecificParkingLot(@PathVariable String parkingLotName){
        return parkingLotService.getSpecificParkingLot(parkingLotName);
    }
}
