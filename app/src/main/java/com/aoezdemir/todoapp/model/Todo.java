package com.aoezdemir.todoapp.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.aoezdemir.todoapp.crud.database.TodoDBHelper;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Todo implements Serializable {

    private final static String DEFAULT_EXPIRY = "--";

    private Long id;
    private String name;
    private String description;
    private Long expiry;
    private Boolean done;
    private Boolean favourite;
    private List<String> contacts;
    private byte[] image;
    private String price;
    private double lang;
    private double lat ;

    public Todo() {
        super();
        contacts = new ArrayList<>();
    }

    public Todo(Long id, String name, String description, Long expiry, Boolean done,
                Boolean favourite, List<String> contacts,byte[] image,String price,double lang,double lat) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.expiry = expiry;
        this.done = done;
        this.favourite = favourite;
        this.contacts = contacts;
        this.image = image;
        this.price = price;
        this.lat = lat;
        this.lang = lang;
    }

    public static Todo createFrom(Cursor cursorTodo) {
        Long id = cursorTodo.getLong(0);
        String name = cursorTodo.getString(1);
        String description = cursorTodo.getString(2);
        Long expiry = cursorTodo.getLong(3);
        Boolean done = cursorTodo.getInt(4) != 0;
        Boolean favourite = cursorTodo.getInt(5) != 0;
        String contactsdb = cursorTodo.getString(6);
        byte[] image = cursorTodo.getBlob(7);
        String price = cursorTodo.getString(8);
        double lang = cursorTodo.getDouble(9);
        double lat = cursorTodo.getDouble(10);
        List<String> contacts = contactsdb != null && !contactsdb.isEmpty() ?
                new ArrayList<>(Arrays.asList(contactsdb.split(","))) :
                new ArrayList<>();
        return new Todo(id, name, description, expiry, done, favourite, contacts, image,price,lang,lat);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public double getLang() {
        return lang;
    }
    public double getLat() {
        return lat;
    }
    public void setLang(double lang) {
        this.lang =  lang;
    }
    public void setLat(double lat) {
        this.lat= lat;
    }

    public String getPrice() {
        return price;
    }

    public String getPriceWithCurr() {
        return "NPR " + price + "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getExpiry() {
        return expiry;
    }

    public void setExpiry(Long expiry) {
        this.expiry = expiry;
    }

    public boolean isExpired() {
        return new Date(expiry).before(new Date());
    }

    public String formatExpiry() {
        return expiry != null ? new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMAN).format(new Date(expiry)) : DEFAULT_EXPIRY;
    }

    public Boolean isDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    public void addContact(String contact) {
        if (contacts == null) {
            contacts = new ArrayList<>();
        }
        if (!contacts.contains(contact)) {
            contacts.add(contact);
        }
    }

    public byte[] setImage(byte[] image){
       return this.image = image;
    }

    public byte[] getImageFromDb(){
        return this.image;
    }





    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Todo && this.id.equals(((Todo) object).getId());
    }

    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        if (id != null) {
            cv.put(TodoDBHelper.COL_TODO_ID, id);
        }
        cv.put(TodoDBHelper.COL_TODO_NAME, name);
        cv.put(TodoDBHelper.COL_TODO_DESCRIPTION, description);
        cv.put(TodoDBHelper.COL_TODO_EXPIRY, expiry);
        cv.put(TodoDBHelper.COL_TODO_DONE, done ? 1 : 0);
        cv.put(TodoDBHelper.COL_TODO_FAVOURITE, favourite ? 1 : 0);
        cv.put(TodoDBHelper.COL_TODO_IMAGES,image);
        cv.put(TodoDBHelper.COL_TODO_PRICE,price);
        cv.put(TodoDBHelper.COL_TODO_LANG,lang);
        cv.put(TodoDBHelper.COL_TODO_LAT,lat);
        cv.put(TodoDBHelper.COL_TODO_CONTACTS, contacts == null ? null : android.text.TextUtils.join(",", contacts));
        return cv;
    }

    @Override
    public String toString() {
        return "Todo {id = " + id + ", name = " + name + ", description = " + description +
                ", expiry = " + expiry + ", done = " + done + ", image = " + image + " favourite = " + favourite +
                ", contacts = " + android.text.TextUtils.join(",", contacts) + "}";
    }
}