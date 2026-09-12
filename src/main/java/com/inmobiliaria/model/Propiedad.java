package com.inmobiliaria.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Representa un inmueble publicado por una inmobiliaria.
 * Agrupa además sus relaciones 1:N (imágenes) y N:M (características)
 * para facilitar el armado de la ficha de detalle.
 */
public class Propiedad {

    private int idPropiedad;
    private int idUsuarioInmobiliaria;
    private int idCiudad;
    private int idTipoPropiedad;
    private String matriculaInmobiliaria;
    private String titulo;
    private String descripcion;
    private BigDecimal precio;
    private String direccion;
    private BigDecimal areaM2;
    private String estado; // DISPONIBLE, RESERVADA, VENDIDA, ARRENDADA, INACTIVA
    private String fechaPublicacion;

    // Campos derivados de los JOIN (solo lectura, para listados/fichas)
    private String nombreCiudad;
    private String departamentoCiudad;
    private String nombreTipoPropiedad;
    private String nombreComercialInmobiliaria;
    private String telefonoContactoInmobiliaria;
    private long totalCitas;

    private List<ImagenPropiedad> imagenes;
    private List<Caracteristica> caracteristicas;

    public int getIdPropiedad() { return idPropiedad; }
    public void setIdPropiedad(int idPropiedad) { this.idPropiedad = idPropiedad; }

    public int getIdUsuarioInmobiliaria() { return idUsuarioInmobiliaria; }
    public void setIdUsuarioInmobiliaria(int idUsuarioInmobiliaria) { this.idUsuarioInmobiliaria = idUsuarioInmobiliaria; }

    public int getIdCiudad() { return idCiudad; }
    public void setIdCiudad(int idCiudad) { this.idCiudad = idCiudad; }

    public int getIdTipoPropiedad() { return idTipoPropiedad; }
    public void setIdTipoPropiedad(int idTipoPropiedad) { this.idTipoPropiedad = idTipoPropiedad; }

    public String getMatriculaInmobiliaria() { return matriculaInmobiliaria; }
    public void setMatriculaInmobiliaria(String matriculaInmobiliaria) { this.matriculaInmobiliaria = matriculaInmobiliaria; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public BigDecimal getAreaM2() { return areaM2; }
    public void setAreaM2(BigDecimal areaM2) { this.areaM2 = areaM2; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(String fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public String getNombreCiudad() { return nombreCiudad; }
    public void setNombreCiudad(String nombreCiudad) { this.nombreCiudad = nombreCiudad; }

    public String getDepartamentoCiudad() { return departamentoCiudad; }
    public void setDepartamentoCiudad(String departamentoCiudad) { this.departamentoCiudad = departamentoCiudad; }

    public String getNombreTipoPropiedad() { return nombreTipoPropiedad; }
    public void setNombreTipoPropiedad(String nombreTipoPropiedad) { this.nombreTipoPropiedad = nombreTipoPropiedad; }

    public String getNombreComercialInmobiliaria() { return nombreComercialInmobiliaria; }
    public void setNombreComercialInmobiliaria(String nombreComercialInmobiliaria) { this.nombreComercialInmobiliaria = nombreComercialInmobiliaria; }

    public String getTelefonoContactoInmobiliaria() { return telefonoContactoInmobiliaria; }
    public void setTelefonoContactoInmobiliaria(String telefonoContactoInmobiliaria) { this.telefonoContactoInmobiliaria = telefonoContactoInmobiliaria; }

    public long getTotalCitas() { return totalCitas; }
    public void setTotalCitas(long totalCitas) { this.totalCitas = totalCitas; }

    public List<ImagenPropiedad> getImagenes() { return imagenes; }
    public void setImagenes(List<ImagenPropiedad> imagenes) { this.imagenes = imagenes; }

    public List<Caracteristica> getCaracteristicas() { return caracteristicas; }
    public void setCaracteristicas(List<Caracteristica> caracteristicas) { this.caracteristicas = caracteristicas; }
    
    private Integer alcobas;
    private String banos;
    private Integer anioConstruccion;
    private BigDecimal areaTerreno;
    private BigDecimal areaPrivada;
    private Integer estrato;
    private BigDecimal valorAdministracion;
    private String tipoNegocio;
    private String zonaBarrio;
    private String codigo;
    private String condicion;

    public Integer getAlcobas() { return alcobas; }
    public void setAlcobas(Integer alcobas) { this.alcobas = alcobas; }

    public String getBanos() { return banos; }
    public void setBanos(String banos) { this.banos = banos; }

    public Integer getAnioConstruccion() { return anioConstruccion; }
    public void setAnioConstruccion(Integer anioConstruccion) { this.anioConstruccion = anioConstruccion; }

    public BigDecimal getAreaTerreno() { return areaTerreno; }
    public void setAreaTerreno(BigDecimal areaTerreno) { this.areaTerreno = areaTerreno; }

    public BigDecimal getAreaPrivada() { return areaPrivada; }
    public void setAreaPrivada(BigDecimal areaPrivada) { this.areaPrivada = areaPrivada; }

    public Integer getEstrato() { return estrato; }
    public void setEstrato(Integer estrato) { this.estrato = estrato; }

    public BigDecimal getValorAdministracion() { return valorAdministracion; }
    public void setValorAdministracion(BigDecimal valorAdministracion) { this.valorAdministracion = valorAdministracion; }

    public String getTipoNegocio() { return tipoNegocio; }
    public void setTipoNegocio(String tipoNegocio) { this.tipoNegocio = tipoNegocio; }

    public String getZonaBarrio() { return zonaBarrio; }
    public void setZonaBarrio(String zonaBarrio) { this.zonaBarrio = zonaBarrio; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getCondicion() { return condicion; }
    public void setCondicion(String condicion) { this.condicion = condicion; }

    /** Devuelve la URL de portada, o la primera imagen disponible, o un placeholder. */
    public String getUrlPortada() {
        if (imagenes == null || imagenes.isEmpty()) {
            return "https://images.unsplash.com/photo-1560518883-ce09059eeffa?auto=format&fit=crop&w=600&q=60";
        }
        return imagenes.stream()
                .filter(ImagenPropiedad::isEsPortada)
                .findFirst()
                .orElse(imagenes.get(0))
                .getUrlImagen();
    }
}