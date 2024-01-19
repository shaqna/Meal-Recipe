package com.maou.themeal.data.mapper

import android.util.Log
import com.maou.themeal.R
import com.maou.themeal.data.local.entity.RecipeEntity
import com.maou.themeal.data.remote.response.AreaResponse
import com.maou.themeal.data.remote.response.AreasResponse
import com.maou.themeal.data.remote.response.CategoriesResponse
import com.maou.themeal.data.remote.response.CategoryResponse
import com.maou.themeal.data.remote.response.MealRecipeResponse
import com.maou.themeal.data.remote.response.MealsFilterResponse
import com.maou.themeal.data.remote.response.MealsInfoResponse
import com.maou.themeal.model.Area
import com.maou.themeal.model.Areas
import com.maou.themeal.model.Categories
import com.maou.themeal.model.Category
import com.maou.themeal.model.MealFilter
import com.maou.themeal.model.MealInfo
import com.maou.themeal.model.MealRecipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.StringBuilder
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties


fun CategoriesResponse.toModel(): Categories {
    return Categories(
        categories = categories.toListCategoryModel()
    )
}

private fun List<CategoryResponse>.toListCategoryModel(): List<Category> {
    return map {
        Category(
            category = it.strCategory,
            id = it.idCategory,
            thumb = it.strCategoryThumb,
            description = it.strCategoryDescription
        )
    }
}


fun AreasResponse.toModel(): Areas {
    return Areas(
        areas = meals.toListAreaModel()
    )
}

private fun List<AreaResponse>.toListAreaModel(): List<Area> {
    return map {
        var imageArea = ""
        var description = ""
        if (it.strArea == "American") {
            imageArea =
                "https://cdn.vectorstock.com/i/1000x1000/58/86/different-common-american-food-set-vector-7695886.webp"
            description =
                "Highlights of American cuisine include milkshakes, barbecue, and a wide range of fried foods. Many quintessential American dishes are unique takes on food originally from other culinary traditions, including pizza, hot dogs, and Tex-Mex."
        } else if (it.strArea == "Canadian") {
            imageArea =
                "https://canadianfoodfocus.org/wp-content/uploads/2021/03/cultural-cuisine.jpg"
            description =
                "Illustrating the French influence, much of Canadian cuisine is rich and heavily spiced. It's also often heavy in carbohydrates, such as bread and potatoes, as well as game meats, such as hare and venison. Unsurprisingly, due to the cold climate, it also features a wide array of soups and stews."
        }

        Area(area = it.strArea, imageArea, description)
    }
}

fun MealsFilterResponse.toModel(): MealFilter {
    return MealFilter(
        mealsFilter = meals.toListMealInfoModel()
    )
}

private fun List<MealsInfoResponse>.toListMealInfoModel(): List<MealInfo> {
    return map {
        MealInfo(
            meal = it.strMeal,
            mealThumb = it.strMealThumb,
            id = it.idMeal
        )
    }
}

fun MealRecipeResponse.toModel(): MealRecipe {
    val meals = meals[0]

    Log.d("Mapper", meals.toString())
    val ingredients: StringBuilder = StringBuilder().append("")
    val measures: StringBuilder = StringBuilder().append("")

    for (n in 1..20) {
        val ingredientProperty = readInstanceProperty<String?>(meals, "strIngredient$n")

        if (ingredientProperty?.trim() != "" && ingredientProperty?.trim() != null) ingredients.append(
            "\n \u2022 $ingredientProperty"
        )

        val measureProperty = readInstanceProperty<String?>(meals, "strMeasure$n")
        if (measureProperty?.trim() != "" && ingredientProperty?.trim() != null) measures.append("\n : $measureProperty")
    }


    return MealRecipe(
        id = meals.idMeal.toString(),
        meal = meals.strMeal.toString(),
        category = meals.strCategory.toString(),
        area = meals.strArea.toString(),
        instructions = meals.strInstructions.toString(),
        mealThumb = meals.strMealThumb.toString(),
        tags = meals.strTags.toString(),
        youtube = meals.strYoutube.toString(),
        source = meals.strSource.toString(),
        ingredients = ingredients.toString(),
        measures = measures.toString()
    )
}

fun MealRecipeResponse.toListModel(): List<MealRecipe> {
    val recipeList = mutableListOf<MealRecipe>()

    meals.forEach {
        Log.d("Mapper", it.toString())
        val ingredients: StringBuilder = StringBuilder().append("")
        val measures: StringBuilder = StringBuilder().append("")

        for (n in 1..20) {
            val ingredientProperty = readInstanceProperty<String?>(it, "strIngredient$n")

            if (ingredientProperty?.trim() != "" && ingredientProperty?.trim() != null) ingredients.append(
                "\n \u2022 $ingredientProperty"
            )

            val measureProperty = readInstanceProperty<String?>(it, "strMeasure$n")
            if (measureProperty?.trim() != "" && ingredientProperty?.trim() != null) measures.append(
                "\n : $measureProperty"
            )
        }

        val recipe = MealRecipe(
            id = it.idMeal.toString(),
            meal = it.strMeal.toString(),
            category = it.strCategory.toString(),
            area = it.strArea.toString(),
            instructions = it.strInstructions.toString(),
            mealThumb = it.strMealThumb.toString(),
            tags = it.strTags.toString(),
            youtube = it.strYoutube.toString(),
            source = it.strSource.toString(),
            ingredients = ingredients.toString(),
            measures = measures.toString()
        )

        recipeList.add(recipe)
    }


    return recipeList
}

fun Flow<List<RecipeEntity>>.toFlowListModel(): Flow<List<MealRecipe>> =
    map {
        it.toListModel()
    }

private fun List<RecipeEntity>.toListModel(): List<MealRecipe> =
    map {
        it.toModel()
    }

private fun RecipeEntity.toModel(): MealRecipe {
    return MealRecipe(
        id = id,
        meal = meal,
        mealThumb = mealThumb,
        area = area,
        instructions = instructions,
        ingredients = ingredients,
        isFavorite = isFavorite,
        youtube = youtube,
        source = source,
        tags = tags,
        measures = measures,
        category = category
    )
}

@Suppress("UNCHECKED_CAST")
fun <R> readInstanceProperty(instance: Any, propertyName: String): R {
    val property = instance::class.members
        // don't cast here to <Any, R>, it would succeed silently
        .first { it.name == propertyName } as KProperty1<Any, *>
    // force a invalid cast exception if incorrect type here
    return property.get(instance) as R
}