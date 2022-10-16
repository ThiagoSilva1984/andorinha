package model.seletor;

import java.util.Calendar;

public class TweetSeletor {

	private Integer id;
	private String conteudo;
	private Calendar data_postagem;
	private Calendar data_postagem_final;
	private Integer idUsuario;

	private Integer limite;
	private Integer pagina;

	public boolean possuiFiltro() {
		return ( this.id != null ) || 
			   ( this.conteudo != null && !this.conteudo.trim().isEmpty() ) ||
			   ( this.getData_postagem() != null ) ||
			   ( this.getIdUsuario() != null ) ||
			   ( this.getData_postagem_final() != null );
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

	public Calendar getData_postagem() {
		return data_postagem;
	}

	public void setData_postagem(Calendar data_postagem) {
		this.data_postagem = data_postagem;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Calendar getData_postagem_final() {
		return data_postagem_final;
	}

	public void setData_postagem_final(Calendar data_postagem_final) {
		this.data_postagem_final = data_postagem_final;
	}

	public Integer getLimite() {
		return limite;
	}

	public void setLimite(Integer limite) {
		this.limite = limite;
	}

	public Integer getPagina() {
		return pagina;
	}

	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}

}
