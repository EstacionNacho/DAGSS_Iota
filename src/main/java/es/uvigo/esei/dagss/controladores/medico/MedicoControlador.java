/*
 Proyecto Java EE, DAGSS-2013
 */
package es.uvigo.esei.dagss.controladores.medico;

import es.uvigo.esei.dagss.controladores.autenticacion.AutenticacionControlador;
import es.uvigo.esei.dagss.dominio.daos.MedicoDAO;
import es.uvigo.esei.dagss.dominio.entidades.Medico;
import es.uvigo.esei.dagss.dominio.entidades.TipoUsuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

//Imports nuevos
import es.uvigo.esei.dagss.dominio.entidades.Cita;
import es.uvigo.esei.dagss.dominio.daos.CitaDAO;
import es.uvigo.esei.dagss.dominio.daos.PrescripcionDAO;
import es.uvigo.esei.dagss.dominio.entidades.Paciente;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author ribadas
 */

@Named(value = "medicoControlador")
@SessionScoped
public class MedicoControlador implements Serializable {

    private Medico medicoActual;
    private String dni;
    private String numeroColegiado;
    private String password;
    
    //Atributos nuevos
    private Cita citaActual;
    private List<Cita> citas;
    private Cita citaDetalle;
    private Paciente pacienteActual;
    private List<Prescripcion> prescripciones;

    @Inject
    private AutenticacionControlador autenticacionControlador;
    
    
     //Injects nuevos
    @Inject
    private CitaDAO citaDAO;
    @Inject
    private PrescripcionDAO prescripcionDAO;

    
    @EJB
    private MedicoDAO medicoDAO;

    /**
     * Creates a new instance of AdministradorControlador
     */
    public MedicoControlador() {
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(String numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Medico getMedicoActual() {
        return medicoActual;
    }

    public void setMedicoActual(Medico medicoActual) {
        this.medicoActual = medicoActual;
    }

    private boolean parametrosAccesoInvalidos() {
        return (((dni == null) && (numeroColegiado == null)) || (password == null));
    }

    private Medico recuperarDatosMedico() {
        Medico medico = null;
        if (dni != null) {
            medico = medicoDAO.buscarPorDNI(dni);
        }
        if ((medico == null) && (numeroColegiado != null)) {
            medico = medicoDAO.buscarPorNumeroColegiado(numeroColegiado);
        }
        return medico;
    }

    public String doLogin() {
        String destino = null;
        if (parametrosAccesoInvalidos()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se ha indicado un DNI ó un número de colegiado o una contraseña", ""));
        } else {
            Medico medico = recuperarDatosMedico();
            if (medico == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No existe ningún médico con los datos indicados", ""));
            } else {
                if (autenticacionControlador.autenticarUsuario(medico.getId(), password,
                        TipoUsuario.MEDICO.getEtiqueta().toLowerCase())) {
                    medicoActual = medico;
                    destino = "privado/index";
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Credenciales de acceso incorrectas", ""));
                }
            }
        }
        return destino;
    }

    public String gotoAtencion(Cita cita){
        
        String destino = "index";
        citaActual = cita;
        
        if(citaActual.getEstado().getEtiqueta().equals("PLANIFICADA")){
            prescripciones = prescripcionDAO.buscarPorPaciente(citaActual.getPaciente().getNumeroTarjetaSanitaria());
            destino = "FormularioAtencion";
        }
        
        return destino;
    }
    
    public Cita getCitaActual(){
        Long id = new Long(1);
        citaActual = citaDAO.buscarPorId(id);
        return citaActual;
    }
    public void setCitaActual(Cita cita){
        this.citaActual = cita;
    }
    
    //Acciones
    public String doShowCita(Cita cita) {
        
        citaActual = cita;
        
        return "detallesCita";
    }
    
    public String getFechaActual(){
       Date date = Calendar.getInstance().getTime();
       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
       String d = dateFormat.format(date);
       return d;
    }
    
    public Date convertStringFecha(String d) throws ParseException{
        DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dFormat.parse(d);
        return date;
}
    
    public String doGetCitasMedico() throws ParseException{
       String fechaActual = this.getFechaActual();
       Date stringFechaActual = this.convertStringFecha(fechaActual);
       
      //citas = citaDAO.buscarCitasPorMedicoDia(medicoActual.getId(), stringFechaActual);
      
      citas = citaDAO.buscarCitasPorMedico(medicoActual.getId());
       
       return "agenda";        
    }    
    
    public List<Cita> getCitas(){
        return citas;
    }
    
    public List<Prescripcion> getPrescripciones(){
        return prescripciones;
    }
    
    public void setCitas(List<Cita> citas){
        this.citas = citas;
    }
    
    public Cita getCitaDetalle(){
        return citaActual;
    }
    
    public void setCitaDetalle(Cita citaDetalle){
        this.citaActual=citaDetalle;
    }    
    
    public String doActualizarEstadoCita() throws ParseException{
        citaDAO.actualizarEstadoCita(citaActual);
        return this.doGetCitasMedico();
    }
    
    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }
    
    public String detalleMedico(Medico medico){
        this.medicoActual=medico;
        return "./detalleMedico.xhtml";
    }
    
    public String goToAgenda() throws ParseException{
       
       citas = citaDAO.buscarCitasPorMedico(medicoActual.getId());
       
       return "agenda";        
    }
    
}
