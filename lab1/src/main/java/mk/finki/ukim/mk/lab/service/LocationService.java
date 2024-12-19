package mk.finki.ukim.mk.lab.service;

import mk.finki.ukim.mk.lab.model.Location;
import org.springframework.stereotype.Service;

import java.util.List;
public interface LocationService {
    public List<Location> findAll();
    public Location findByID(Long id);
}
