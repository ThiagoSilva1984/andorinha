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
		tweet.setData_postagem(Calendar.getInstance());
		super.em.persist(tweet);
	}

	public void atualizar(Tweet tweet) {
		tweet.setData_postagem(Calendar.getInstance());
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

			if (seletor.getData_postagem() != null && seletor.getData_postagem_final() == null) {
				if (!primeiro) {
					jpql.append("AND ");
				}
				jpql.append("t.data_postagem = :data_postagem ");
				primeiro = false;
			}

			if (seletor.getData_postagem() != null && seletor.getData_postagem_final() != null) {
				if (!primeiro) {
					jpql.append("AND ");
				}
				jpql.append("t.data_postagem BETWEEN :data_postagem AND :data_postagem_final ");
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

			if (seletor.getData_postagem() != null) {
				query.setParameter("data_postagem", seletor.getData_postagem());
			}

			if (seletor.getData_postagem() != null && seletor.getData_postagem_final() != null) {
				query.setParameter("data_postagem", seletor.getData_postagem());
				query.setParameter("data_postagem_final", seletor.getData_postagem_final());
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
