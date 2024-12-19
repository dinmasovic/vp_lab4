package mk.finki.ukim.mk.lab.web.controller;

import mk.finki.ukim.mk.lab.model.Event;
import mk.finki.ukim.mk.lab.model.Location;
import mk.finki.ukim.mk.lab.service.impl.EventServiceImpl;
import mk.finki.ukim.mk.lab.service.impl.LocationServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/events/edit/{eventId}")
public class EventEditController {
    private LocationServiceImpl locations;
    private EventServiceImpl obj;

    public EventEditController(LocationServiceImpl locations, EventServiceImpl obj) {
        this.locations = locations;
        this.obj = obj;
    }

    @PostMapping
    public String getEventsPage(@PathVariable Long eventId, @RequestParam String editName,
                                @RequestParam String editRate, @RequestParam String editDes,
                                @RequestParam(required = false,defaultValue = "7") String editLocation ,Model model) {
        Event event = obj.findById(eventId);
        List<Event> events=obj.listAll();
        List<String> eventNames=events.stream().map(s->s.name).toList();
        List<Long> eventLocationIds=events.stream().map(Event::getLocationId).toList();
        if(eventNames.contains(editName) && eventLocationIds.contains(Long.parseLong(editLocation))){
            return "redirect:/events/edit/{eventId}";
        }
        if(editName !=null)
            event.setName(editName);
        if(editRate!=null)
            event.setPopularityScore(Double.parseDouble(editRate));
        if(editDes!=null)
            event.setDescription(editDes);
        if(editLocation !=null)
            event.setLocation(locations.findByID(Long.parseLong(editLocation)));
        else {
            Location defaultLocation = locations.findByID(Long.parseLong("7"));
            locations.save(defaultLocation);
            event.setLocation(defaultLocation);
        }
        obj.save(event);
        return "redirect:/events";
    }
    @GetMapping
    public String editEvent(@PathVariable Long eventId,Model model){
        model.addAttribute("locations",locations.findAll());
        model.addAttribute("rate",obj.findById(eventId).popularityScore);
        model.addAttribute("des",obj.findById(eventId).description);
        model.addAttribute("name",obj.findById(eventId).name);
        //obj.findById(eventId).name="da";
        return "add-event";
    }
}
