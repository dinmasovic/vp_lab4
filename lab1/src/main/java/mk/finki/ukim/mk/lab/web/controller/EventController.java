package mk.finki.ukim.mk.lab.web.controller;

import jakarta.servlet.http.HttpSession;
import mk.finki.ukim.mk.lab.service.LocationService;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import mk.finki.ukim.mk.lab.model.Event;
import mk.finki.ukim.mk.lab.service.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {
    private EventService eventService;
    private LocationService locations;
    public EventController(EventService eventService,LocationService locations) {
        this.eventService = eventService;
        this.locations=locations;
    }

    @GetMapping
    public String getEventsPage(@RequestParam(required = false) String error, Model model, HttpServletRequest req){
        String keyword=req.getParameter("searched");
        String rating=req.getParameter("rate");
        List<Event> tmp = eventService.listAll();
        List<Event> filteredEvents = new ArrayList<>();
        if (keyword != null && !keyword.equals("") && rating != null && !rating.equals("")) {
            tmp = eventService.searchEvents(keyword);
            for (Event event : tmp) {
                if (event.getPopularityScore() > Double.parseDouble(rating)) {
                    filteredEvents.add(event);
                }
            }
        } else {
            filteredEvents = tmp;
        }
        model.addAttribute("events",filteredEvents);
        model.addAttribute("a",filteredEvents.size());
        model.addAttribute("locations",locations.findAll());

        return "listEvents";
    }
    @PostMapping
    public String saveEvent(@RequestParam(required = false) String selected, @RequestParam(required = false) String numTickets, HttpSession session, Model model) {

        session.setAttribute("selected", selected);
        session.setAttribute("numTickets", numTickets);

        if (selected != null && numTickets != null && !numTickets.isEmpty()) {
            return "redirect:/eventBooking";
        } else {

            return "redirect:/events";
        }
    }

}