package tacos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import tacos.data.TacoRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TacoFlowTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TacoRepository tacoRepository;

	@Autowired
	private JdbcTemplate jdbc;

	@Test
	void designPageLoadsIngredientsFromEmbeddedDatabase() throws Exception {
		mockMvc.perform(get("/design"))
				.andExpect(status().isOk())
				.andExpect(view().name("design"))
				.andExpect(model().attributeExists("wrap", "protein", "veggies", "cheese", "sauce"));
	}

	@Test
	void invalidDesignReturnsToForm() throws Exception {
		mockMvc.perform(post("/design").param("name", "tiny"))
				.andExpect(status().isOk())
				.andExpect(view().name("design"))
				.andExpect(model().attributeHasFieldErrors("taco", "name", "ingredients"));
	}

	@Test
	void tacoRepositoryPersistsTacoAndIngredients() {
		Taco taco = new Taco();
		taco.setName("Java Taco");
		taco.setIngredients(java.util.List.of(new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP)));

		Taco saved = tacoRepository.save(taco);

		assertThat(saved.getId()).isNotNull();
		assertThat(jdbc.queryForObject("select count(*) from Taco where id = ?", Integer.class, saved.getId()))
				.isEqualTo(1);
		assertThat(jdbc.queryForObject("select count(*) from Taco_Ingredients where taco = ?", Integer.class,
				saved.getId())).isEqualTo(1);
	}
}
