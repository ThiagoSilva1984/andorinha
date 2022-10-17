package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity // tudo que é uma entidade é algo que eu posso salvar no banco de dados
@Table(name = "usuario")
public class Usuario {

	@Id
	@SequenceGenerator(name = "seq_usuario", sequenceName = "seq_usuario", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
	@Column(name = "id")
	private Integer id;

	@Column(name = "nome")
	private String nome;

//	@OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
//	private List<Tweet> tweets;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

//	public List<Tweet> getTweets() {
//		return tweets;
//	}
//
//	public void setTweets(List<Tweet> tweets) {
//		this.tweets = tweets;
//	}

	@Override
	public String toString() {
		return "Id: " + this.getId() + ", Nome: " + this.getNome();
	}

}
