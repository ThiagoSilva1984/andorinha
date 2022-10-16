package model.seletor;

import java.util.Calendar;

public class ComentarioSeletor {

	private Integer id;
	private String conteudo;
	private Calendar data;
	private Calendar data_final;
	private Integer idUsuario;
	private Integer idTweet;
	private Calendar dataTweet;

	private Integer limite;
	private Integer pagina; 
	
	public boolean possuiFiltro() {
		return ( this.id != null ) || 
			   ( this.conteudo != null && !this.conteudo.trim().isEmpty() ) ||
			   ( this.data != null ) ||
			   ( this.data_final != null ) ||
			   ( this.idTweet != null ) ||
			   ( this.idUsuario != null ) ||
			   ( this.dataTweet != null);
	}
	
	public boolean possuiPaginacao() {
		return this.pagina > 0 && this.limite > 0;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Integer getIdTweet() {
		return idTweet;
	}

	public void setIdTweet(Integer idTweet) {
		this.idTweet = idTweet;
	}

	public Integer getLimmite() {
		return limite;
	}

	public void setLimmite(Integer limmite) {
		this.limite = limmite;
	}

	public Integer getPagina() {
		return pagina;
	}

	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}

	public Calendar getDataTweet() {
		return dataTweet;
	}

	public void setDataTweet(Calendar dataTweet) {
		this.dataTweet = dataTweet;
	}

	public Integer getLimite() {
		return limite;
	}

	public void setLimite(Integer limite) {
		this.limite = limite;
	}

	public Calendar getData_final() {
		return data_final;
	}

	public void setData_final(Calendar data_final) {
		this.data_final = data_final;
	}

}
