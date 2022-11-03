package life.hyakusho.share;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {
    record TaskItem(String id, String place, String groupname, String startdatetime, String enddatetime, boolean done) {
    }

    private List<TaskItem> taskItems = new ArrayList<>();
    private final ShareDao dao;

    @Autowired
    HomeController(ShareDao dao) {
        this.dao = dao;
    }

    @RequestMapping(value = "/hello")
    String hello(Model model) {
        model.addAttribute("time", LocalDateTime.now());
        return "hello";
    }

    @GetMapping("/list")
    String listItems(Model model) {
        List<TaskItem> taskItems = dao.findAll();
        model.addAttribute("taskList", taskItems);
        return "home";
    }

    @GetMapping("/add")
    String addItem(@RequestParam("place") String place,
                   @RequestParam("groupname") String groupname,
                   @RequestParam("startdatetime") String startdatetime,
                   @RequestParam("enddatetime") String enddatetime) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        TaskItem item = new TaskItem(id, place, groupname, startdatetime, enddatetime ,false);
        dao.add(item);
        return "redirect:/list";
    }

    @GetMapping("/delete")
    String deleteItem(@RequestParam("id") String id) {
        dao.delete(id);
        return "redirect:/list";
    }

    @GetMapping("/update")
    String updateItem(@RequestParam("id") String id,
                      @RequestParam("place") String place,
                      @RequestParam("groupname") String groupname,
                      @RequestParam("startdatetime") String startdatetime,
                      @RequestParam("enddatetime") String enddatetime,
                      @RequestParam("done") boolean done) {
        TaskItem taskItem = new TaskItem(id, place, groupname, startdatetime, enddatetime, done);
        dao.update(taskItem);
        return "redirect:/list";
    }
}