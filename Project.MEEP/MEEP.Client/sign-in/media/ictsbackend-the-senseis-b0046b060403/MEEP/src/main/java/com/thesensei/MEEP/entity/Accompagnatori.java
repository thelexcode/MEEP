package com.thesensei.MEEP.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Accompagnatori {

    private List<String> nominatiivi= new ArrayList();
    private List<String> email= new ArrayList();

}
