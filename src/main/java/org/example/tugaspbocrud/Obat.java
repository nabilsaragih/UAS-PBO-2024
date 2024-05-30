package org.example.tugaspbocrud;

import javafx.beans.property.SimpleStringProperty;

class Obat {
    private final SimpleStringProperty id;
    private final SimpleStringProperty namaObat;
    private final SimpleStringProperty jenisObat;
    private final SimpleStringProperty golonganObat;
    private final SimpleStringProperty tanggalExpired;

    public Obat(String id, String namaObat, String jenisObat, String golonganObat, String tanggalExpired) {
        this.id = new SimpleStringProperty(id);
        this.namaObat = new SimpleStringProperty(namaObat);
        this.jenisObat = new SimpleStringProperty(jenisObat);
        this.golonganObat = new SimpleStringProperty(golonganObat);
        this.tanggalExpired = new SimpleStringProperty(tanggalExpired);
    }

    public String getId() { return id.get(); }
    public String getNamaObat() { return namaObat.get(); }
    public String getJenisObat() { return jenisObat.get(); }
    public String getGolonganObat() { return golonganObat.get(); }
    public String getTanggalExpired() { return tanggalExpired.get(); }

    public SimpleStringProperty idProperty() { return id; }
    public SimpleStringProperty namaObatProperty() { return namaObat; }
    public SimpleStringProperty jenisObatProperty() { return jenisObat; }
    public SimpleStringProperty golonganObatProperty() { return golonganObat; }
    public SimpleStringProperty tanggalExpiredProperty() { return tanggalExpired; }
}

