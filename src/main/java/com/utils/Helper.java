package com.utils;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import com.entity.Film;
import com.entity.FilmList;
import com.entity.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

public class Helper {
    public String convertFilmToText(Film film) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<ID>").append(film.getId()).append("<TITLE>").append(film.getTitle())
                     .append("<YEAR>").append(film.getYear()).append("<DIRECTOR>").append(film.getDirector())
                     .append("<STARS>").append(film.getStars()).append("<REVIEW>").append(film.getReview());
        return stringBuilder.toString();
    }

    public Film convertTextToFilm(String data) {
        String[] parts = data.split("<REVIEW>");
        String[] stars = parts[0].split("<STARS>");
        String[] director = stars[0].split("<DIRECTOR>");
        String[] year = director[0].split("<YEAR>");
        String[] title = year[0].split("<TITLE>");
        String[] id = title[0].split("<ID>");
        return new Film(Integer.parseInt(id[1]), title[1], Integer.parseInt(year[1]), director[1], stars[1], parts[1]);
    }

    public String convertObject(String dataFormat, Object object) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JAXBContext context = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter writer = new StringWriter();
        if ("json".equals(dataFormat)) {
            return gson.toJson(object);
        } else if ("xml".equals(dataFormat)) {
            marshaller.marshal(object, writer);
            return writer.toString();
        } else if ("text".equals(dataFormat)) {
            if (object instanceof FilmList) {
                StringBuilder textBuilder = new StringBuilder();
                for (Film film : ((FilmList) object).getFilms()) {
                    textBuilder.append(convertFilmToText(film)).append(System.lineSeparator());
                }
                return textBuilder.toString();
            } else if (object instanceof Film) {
                return convertFilmToText((Film) object);
            } else if (object instanceof Response) {
                return ((Response) object).getMessage();
            }
        }
        return gson.toJson(object);
    }

    public String convertFilms(String dataFormat, ArrayList<Film> films) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JAXBContext context = JAXBContext.newInstance(FilmList.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter writer = new StringWriter();
        if ("json".equals(dataFormat)) {
            return gson.toJson(films);
        } else if ("xml".equals(dataFormat)) {
            marshaller.marshal(new FilmList(films), writer);
            return writer.toString();
        } else if ("text".equals(dataFormat)) {
            StringBuilder textBuilder = new StringBuilder();
            for (Film film : films) {
                textBuilder.append(convertFilmToText(film)).append(System.lineSeparator());
            }
            return textBuilder.toString();
        }
        return gson.toJson(films);
    }

    public Film convertToFilm(String format, String str) throws Exception {
        Gson gson = new GsonBuilder().create();
        JAXBContext context = JAXBContext.newInstance(Film.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StringReader reader = new StringReader(str);

        if ("json".equals(format)) {
            return gson.fromJson(str, Film.class);
        } else if ("xml".equals(format)) {
            return (Film) unmarshaller.unmarshal(reader);
        } else if ("text".equals(format)) {
            return convertTextToFilm(str);
        }
        return gson.fromJson(str, Film.class);
    }
}
