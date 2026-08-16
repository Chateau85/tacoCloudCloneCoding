package tacos.web;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import tacos.Ingredient;
import tacos.data.IngredientRepository;

@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {
	private final IngredientRepository ingredientRepo;

	public IngredientByIdConverter(IngredientRepository ingredientRepository) {
		this.ingredientRepo = ingredientRepository;
	}

	@Override
	public Ingredient convert(String id) {
		return ingredientRepo.findById(id);
	}

}
