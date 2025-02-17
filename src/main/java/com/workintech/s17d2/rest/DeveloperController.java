package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.workintech.s17d2.tax.Taxable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {
    private Taxable taxable;
    public Map<Integer, Developer> developers;

    @Autowired
    public DeveloperController(Taxable taxable) {
        this.taxable = taxable;
    }

    @PostConstruct
    public void init() {
        developers = new HashMap<>();
//        Developer dev1 = new JuniorDeveloper(1, "Hakan Sahin", 50000);
//        Developer dev2 = new MidDeveloper(2, "Dilara Atestepe", 80000);
//        Developer dev3 = new SeniorDeveloper(3, "Mahmut Sahin", 150000);
//        developers.put(1, dev1);
//        developers.put(2, dev2);
//        developers.put(3, dev3);
    }

    @GetMapping()
    public List<Developer> getAllDevelopers() {
        return developers.values().stream().toList();
    }

    @GetMapping("/{id}")
    public Developer getDeveloperById(@PathVariable int id){
        return developers.get(id);
    }

    @PostMapping()
    public Developer addDeveloperByExperience(@RequestBody Developer developer) {
        int id = developer.getId();
        String name = developer.getName();
        double salary = developer.getSalary();
        Experience exp = developer.getExperience();

        if (exp == Experience.JUNIOR) {
            double newSalaryJunior = salary - (salary * taxable.getSimpleTaxRate());
            developers.put(id, new JuniorDeveloper(id, name, newSalaryJunior));
        } else if (exp == Experience.MID) {
            double newSalaryMid = salary - (salary * taxable.getMiddleTaxRate());
            developers.put(id, new MidDeveloper(id, name, newSalaryMid));
        } else if (exp == Experience.SENIOR) {
            double newSalarySenior = salary - (salary * taxable.getUpperTaxRate());
            developers.put(id, new SeniorDeveloper(id, name, newSalarySenior));
        }
        return developers.get(id);
    }

    @PutMapping("/{id}")
    public Developer updateDeveloper(@PathVariable int id, @RequestBody Developer updatedDeveloper) {
        developers.put(id,updatedDeveloper);
        return developers.get(id);
    }

    @DeleteMapping("/{id}")
    public Developer removeById(@PathVariable int id)    {
        Developer developer = developers.get(id);
        developers.remove(id);
        return developer;
    }
}
