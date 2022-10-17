package repository;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import model.Tweet;
import model.seletor.TweetSeletor;

@Stateless
public class TweetRepository extends AbstractCrudRepository {

	public void inserir(Tweet tweet) {
		tweet.setDataPostagem(Calendar.getInstance());
		super.em.persist(tweet);
	}

	public void atualizar(Tweet tweet) {
		tweet.setDataPostagem(Calendar.getInstance());
		super.em.merge(tweet);
	}

	public void remover(int id) {
		Tweet t = this.consultar(id);
		super.em.remove(t);
	}

	public Tweet consultar(int id) {
		return super.em.find(Tweet.class, id);
	}

	public List<Tweet> listarTodos() {
		TweetSeletor seletor = new TweetSeletor();
		return this.pesquisar(seletor);
	}

	public List<Tweet> pesquisar(TweetSeletor seletor) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT t FROM Tweet t ");

		this.criarFiltro(jpql, seletor);

		Query query = super.em.createQuery(jpql.toString());

		this.adicionarParametros(query, seletor);

		return query.getResultList();
	}

	private void criarFiltro(StringBuilder jpql, TweetSeletor seletor) {

		if (seletor.possuiFiltro()) {
			jpql.append("WHERE ");
			boolean primeiro = true;

			if (seletor.getId() != null) {
				jpql.append("t.id = :id ");
				primeiro = false;
			}

			if (seletor.getConteudo() != null && !seletor.getConteudo().trim().isEmpty()) {
				if (!primeiro) {
					jpql.append("AND ");
				}
				jpql.append("t.conteudo like :conteudo ");
				primeiro = false;
			}

			if (seletor.getDataPostagemInicial() != null && seletor.getDataPostagemFinal() == null) {
				if (!primeiro) {
					jpql.append("AND ");
				}
				jpql.append("t.dataPostagem = :dataPostagemInicial ");
				primeiro = false;
			}

			if (seletor.getDataPostagemInicial() != null && seletor.getDataPostagemFinal() != null) {
				if (!primeiro) {
					jpql.append("AND ");
				}
				jpql.append("t.dataPostagem BETWEEN :dataPostagemInicial AND :dataPostagemFinal ");
				primeiro = false;
			}
		}
	}

	private void adicionarParametros(Query query, TweetSeletor seletor) {

		if (seletor.possuiFiltro()) {
			if (seletor.getId() != null) {
				query.setParameter("id", seletor.getId());
			}

			if (seletor.getConteudo() != null && !seletor.getConteudo().trim().isEmpty()) {
				query.setParameter("conteudo", String.format("%%%s%%", seletor.getConteudo()));
			}

			if (seletor.getDataPostagemInicial() != null) {
				query.setParameter("dataPostagemInicial", seletor.getDataPostagemInicial());
			}

			if (seletor.getDataPostagemInicial() != null && seletor.getDataPostagemFinal() != null) {
				query.setParameter("dataPostagemInicial", seletor.getDataPostagemInicial());
				query.setParameter("dataPostagemFinal", seletor.getDataPostagemFinal());
			}
		}
	}

	public Long contar(TweetSeletor seletor) {

		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT COUNT(t) FROM Tweet t ");

		this.criarFiltro(jpql, seletor);

		Query query = super.em.createQuery(jpql.toString());

		this.adicionarParametros(query, seletor);

		return (Long) query.getSingleResult();
	}

}
