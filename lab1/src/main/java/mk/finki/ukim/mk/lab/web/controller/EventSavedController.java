package mk.finki.ukim.mk.lab.web.controller;

import mk.finki.ukim.mk.lab.model.Event;
import mk.finki.ukim.mk.lab.service.EventService;
import mk.finki.ukim.mk.lab.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/events/add")
public class EventSavedController {
    private EventService obj;
    private LocationService locations;

    public EventSavedController(EventService obj, LocationService locations) {
        this.obj = obj;
        this.locations = locations;
    }

    @PostMapping
    public String saveEvent(@RequestParam String editName,@RequestParam String editDes,@RequestParam String editRate,@RequestParam String editLocation){
        Event newEvent=new Event(editName,editDes,Double.parseDouble(editRate));
        newEvent.setLocation(locations.findByID(Long.parseLong(editLocation)));
        obj.addEvent(newEvent);
        return "redirect:/events";
    }
    @GetMapping
    public String getHtml(Model model){
        model.addAttribute("locations",locations.findAll());
        return "add-event";
    }


}
