package com.teenteen.teencash.presentation.utills

import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.CategoryName

enum class Icons(val iconPath: Int , val id: Int) {
    BUS(R.drawable.ic_bus , 0),
    CAR(R.drawable.ic_car , 1),
    PARKING(R.drawable.ic_parking , 2),
    SCHOOL(R.drawable.ic_school , 3),
    BOOK(R.drawable.ic_book , 4),
    HOME(R.drawable.ic_home_category , 5),
    DISH(R.drawable.ic_dish , 6),
    COFFEE(R.drawable.ic_coffee , 7),
    GIFT(R.drawable.ic_gift , 8),
    TICKET(R.drawable.ic_ticket , 9),
    TRAVEL(R.drawable.ic_travel , 10),
    PALETTE(R.drawable.ic_palette , 11),
    SHOPPING_BAG(R.drawable.ic_shopping_bag , 12),
    SHOPPING_CART(R.drawable.ic_shopping_cart , 13),
    SPORT(R.drawable.ic_sport , 14),
    SPOTIFY(R.drawable.ic_spotify , 15),
    STEAM(R.drawable.ic_steam , 16),
    TWITCH(R.drawable.ic_twitch , 17),
    ROCK(R.drawable.ic_rock , 18),
    PARTY(R.drawable.ic_party , 19),
    KIOSK(R.drawable.ic_kiosk , 20),
    FIREBASE(R.drawable.ic_firebase , 21),
    DIPLOMACY(R.drawable.ic_diplomacy , 22),
    CANDY(R.drawable.ic_candy , 23),
    BOTTLE(R.drawable.ic_bottle , 24),
    ANDROID(R.drawable.ic_android , 25)
}

object IconType {
    fun getProjectIconType(iconId: Int): Int {
        val icon = when (iconId) {
            Icons.BUS.id -> Icons.BUS.iconPath
            Icons.CAR.id -> Icons.CAR.iconPath
            Icons.PARKING.id -> Icons.PARKING.iconPath
            Icons.SCHOOL.id -> Icons.SCHOOL.iconPath
            Icons.BOOK.id -> Icons.BOOK.iconPath
            Icons.HOME.id -> Icons.HOME.iconPath
            Icons.DISH.id -> Icons.DISH.iconPath
            Icons.COFFEE.id -> Icons.COFFEE.iconPath
            Icons.GIFT.id -> Icons.GIFT.iconPath
            Icons.TICKET.id -> Icons.TICKET.iconPath
            Icons.TRAVEL.id -> Icons.TRAVEL.iconPath
            Icons.PALETTE.id -> Icons.PALETTE.iconPath
            Icons.SHOPPING_BAG.id -> Icons.SHOPPING_BAG.iconPath
            Icons.SHOPPING_CART.id -> Icons.SHOPPING_CART.iconPath
            Icons.SPORT.id -> Icons.SPORT.iconPath
            Icons.SPOTIFY.id -> Icons.SPOTIFY.iconPath
            Icons.STEAM.id -> Icons.STEAM.iconPath
            Icons.TWITCH.id -> Icons.TWITCH.iconPath
            Icons.ROCK.id -> Icons.ROCK.iconPath
            Icons.PARTY.id -> Icons.PARTY.iconPath
            Icons.KIOSK.id -> Icons.KIOSK.iconPath
            Icons.FIREBASE.id -> Icons.FIREBASE.iconPath
            Icons.DIPLOMACY.id -> Icons.DIPLOMACY.iconPath
            Icons.CANDY.id -> Icons.CANDY.iconPath
            Icons.BOTTLE.id -> Icons.BOTTLE.iconPath
            Icons.ANDROID.id -> Icons.ANDROID.iconPath
            else -> Icons.BUS.id
        }
        return icon
    }

    val iconArray = mutableListOf<CategoryName>().apply {
        add(CategoryName(Icons.BUS.id, Icons.BUS.iconPath.toString()))
        add(CategoryName(Icons.CAR.id, Icons.CAR.iconPath.toString()))
        add(CategoryName(Icons.PARKING.id, Icons.PARKING.iconPath.toString()))
        add(CategoryName(Icons.SCHOOL.id, Icons.SCHOOL.iconPath.toString()))
        add(CategoryName(Icons.BOOK.id, Icons.BOOK.iconPath.toString()))
        add(CategoryName(Icons.HOME.id, Icons.HOME.iconPath.toString()))
        add(CategoryName(Icons.DISH.id, Icons.DISH.iconPath.toString()))
        add(CategoryName(Icons.COFFEE.id, Icons.COFFEE.iconPath.toString()))
        add(CategoryName(Icons.GIFT.id, Icons.GIFT.iconPath.toString()))
        add(CategoryName(Icons.TICKET.id, Icons.TICKET.iconPath.toString()))
        add(CategoryName(Icons.TRAVEL.id, Icons.TRAVEL.iconPath.toString()))
        add(CategoryName(Icons.PALETTE.id, Icons.PALETTE.iconPath.toString()))
        add(CategoryName(Icons.SHOPPING_BAG.id, Icons.SHOPPING_BAG.iconPath.toString()))
        add(CategoryName(Icons.SHOPPING_CART.id, Icons.SHOPPING_CART.iconPath.toString()))
        add(CategoryName(Icons.SPORT.id, Icons.SPORT.iconPath.toString()))
        add(CategoryName(Icons.SPOTIFY.id, Icons.SPOTIFY.iconPath.toString()))
        add(CategoryName(Icons.STEAM.id, Icons.STEAM.iconPath.toString()))
        add(CategoryName(Icons.TWITCH.id, Icons.TWITCH.iconPath.toString()))
        add(CategoryName(Icons.ROCK.id, Icons.ROCK.iconPath.toString()))
        add(CategoryName(Icons.PARTY.id, Icons.PARTY.iconPath.toString()))
        add(CategoryName(Icons.KIOSK.id, Icons.KIOSK.iconPath.toString()))
        add(CategoryName(Icons.DIPLOMACY.id, Icons.DIPLOMACY.iconPath.toString()))
        add(CategoryName(Icons.CANDY.id, Icons.CANDY.iconPath.toString()))
        add(CategoryName(Icons.BOTTLE.id, Icons.BOTTLE.iconPath.toString()))
        add(CategoryName(Icons.ANDROID.id, Icons.ANDROID.iconPath.toString()))
        add(CategoryName(Icons.FIREBASE.id, Icons.FIREBASE.iconPath.toString()))
    }
}