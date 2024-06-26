package com.entity;

import java.util.List;
import java.util.ArrayList;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "films")
@XmlAccessorType(XmlAccessType.FIELD)
public class FilmList {
    @XmlElement(name = "film")
    private List<Film> films;

    public FilmList() {
        this.films = new ArrayList<>();
    }

    public FilmList(List<Film> films) {
        this.films = films;
    }

    public List<Film> getFilms() {
        return films;
    }

    public void setFilms(List<Film> films) {
        this.films = films;
    }
}
