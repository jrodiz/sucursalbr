package com.jrodiz.sucursalesbr.obj;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jrodiz.sucursalesbr.base.AppConstants;
import com.jrodiz.sucursalesbr.utils.ParserUtils;

@Entity
public class Sucursal implements Parcelable {


    public Sucursal() {
    }

    @PrimaryKey
    @NonNull
    @SerializedName("ID")
    private String ID;

    @ColumnInfo
    @SerializedName("NOMBRE")
    @Expose
    private String mNombre;

    @ColumnInfo
    @SerializedName("tipo")
    @Expose
    private String tipo;

    @ColumnInfo
    @SerializedName("DOMICILIO")
    @Expose
    private String domicilio;
    @ColumnInfo
    @SerializedName("HORARIO")
    @Expose
    private String horario;
    @ColumnInfo
    @SerializedName("TELEFONO_PORTAL")
    @Expose
    private String telefonoPortal;
    @ColumnInfo
    @SerializedName("TELEFONO_APP")
    @Expose
    private String telefonoApp;
    @ColumnInfo
    @SerializedName("24_HORAS")
    @Expose
    private String fullTime;
    @ColumnInfo
    @SerializedName("SABADOS")
    @Expose
    private String fullSaturday;
    @ColumnInfo
    @SerializedName("CIUDAD")
    @Expose
    private String ciudad;
    @ColumnInfo
    @SerializedName("ESTADO")
    @Expose
    private String estado;
    @ColumnInfo
    @SerializedName("Latitud")
    @Expose
    private String latitud;
    @ColumnInfo
    @SerializedName("Longitud")
    @Expose
    private String longitud;
    @ColumnInfo
    @SerializedName("Correo_Gerente")
    @Expose
    private String correoGerente;
    @ColumnInfo
    @SerializedName("URL_FOTO")
    @Expose
    private String urlFoto;
    @ColumnInfo
    @SerializedName("Suc_Estado_Prioridad")
    @Expose
    private String sucEstadoPrioridad;

    protected Sucursal(Parcel in) {
        ID = in.readString();
        mNombre = in.readString();
        tipo = in.readString();
        domicilio = in.readString();
        horario = in.readString();
        telefonoPortal = in.readString();
        telefonoApp = in.readString();
        fullTime = in.readString();
        fullSaturday = in.readString();
        ciudad = in.readString();
        estado = in.readString();
        latitud = in.readString();
        longitud = in.readString();
        correoGerente = in.readString();
        urlFoto = in.readString();
        sucEstadoPrioridad = in.readString();
        sucCiudadPrioridad = in.readString();
    }

    public static final Creator<Sucursal> CREATOR = new Creator<Sucursal>() {
        @Override
        public Sucursal createFromParcel(Parcel in) {
            return new Sucursal(in);
        }

        @Override
        public Sucursal[] newArray(int size) {
            return new Sucursal[size];
        }
    };

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return mNombre != null? mNombre: "";
    }

    public void setNombre(String nombre) {
        mNombre = nombre;
    }

    public String getTipo() {
        return tipo == null ? "" : tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDomicilio() {
        return domicilio != null ? domicilio : "";
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getTelefonoPortal() {
        return telefonoPortal;
    }

    public void setTelefonoPortal(String telefonoPortal) {
        this.telefonoPortal = telefonoPortal;
    }

    public String getTelefonoApp() {
        return telefonoApp;
    }

    public void setTelefonoApp(String telefonoApp) {
        this.telefonoApp = telefonoApp;
    }

    public String getFullTime() {
        return fullTime;
    }

    public void setFullTime(String fullTime) {
        this.fullTime = fullTime;
    }

    public String getFullSaturday() {
        return fullSaturday;
    }

    public void setFullSaturday(String fullSaturday) {
        this.fullSaturday = fullSaturday;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getLatitudDouble() {
        return ParserUtils.parseDouble(latitud);
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public double getLongitudDouble() {
        return ParserUtils.parseDouble(longitud);
    }
    public String getLongitud() {
        return longitud;
    }


    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getCorreoGerente() {
        return correoGerente;
    }

    public void setCorreoGerente(String correoGerente) {
        this.correoGerente = correoGerente;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getSucEstadoPrioridad() {
        return sucEstadoPrioridad;
    }

    public void setSucEstadoPrioridad(String sucEstadoPrioridad) {
        this.sucEstadoPrioridad = sucEstadoPrioridad;
    }

    public String getSucCiudadPrioridad() {
        return sucCiudadPrioridad;
    }

    public void setSucCiudadPrioridad(String sucCiudadPrioridad) {
        this.sucCiudadPrioridad = sucCiudadPrioridad;
    }

    @ColumnInfo
    @SerializedName("Suc_Ciudad_Prioridad")
    @Expose
    private String sucCiudadPrioridad;

    public LatLng getLatLng() {
        return new LatLng(getLatitudDouble(), getLongitudDouble());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(mNombre);
        dest.writeString(tipo);
        dest.writeString(domicilio);
        dest.writeString(horario);
        dest.writeString(telefonoPortal);
        dest.writeString(telefonoApp);
        dest.writeString(fullTime);
        dest.writeString(fullSaturday);
        dest.writeString(ciudad);
        dest.writeString(estado);
        dest.writeString(latitud);
        dest.writeString(longitud);
        dest.writeString(correoGerente);
        dest.writeString(urlFoto);
        dest.writeString(sucEstadoPrioridad);
        dest.writeString(sucCiudadPrioridad);
    }

    public String getDescription() {
        StringBuilder stringBuilder = new StringBuilder()
                .append(getHorario())
                .append("\n")
                .append(getCiudad()).append(", ").append(getEstado())
                .append("\n\n")
                .append("Mail: ").append(" ").append(getCorreoGerente());
        return stringBuilder.toString();
    }

    public boolean isSucursal() {
        return AppConstants.SUCURSAL.equalsIgnoreCase(getTipo().trim());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sucursal sucursal = (Sucursal) o;

        if (!ID.equals(sucursal.ID)) return false;
        if (mNombre != null ? !mNombre.equals(sucursal.mNombre) : sucursal.mNombre != null)
            return false;
        if (tipo != null ? !tipo.equals(sucursal.tipo) : sucursal.tipo != null) return false;
        if (latitud != null ? !latitud.equals(sucursal.latitud) : sucursal.latitud != null)
            return false;
        return longitud != null ? longitud.equals(sucursal.longitud) : sucursal.longitud == null;
    }

    @Override
    public int hashCode() {
        int result = ID.hashCode();
        result = 31 * result + (mNombre != null ? mNombre.hashCode() : 0);
        result = 31 * result + (tipo != null ? tipo.hashCode() : 0);
        result = 31 * result + (latitud != null ? latitud.hashCode() : 0);
        result = 31 * result + (longitud != null ? longitud.hashCode() : 0);
        return result;
    }
}
