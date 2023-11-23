# memoapp

##Kotlin-sovellusprojekti muistioiden jakamisiin muiden henkilöiden kanssa.

Sovellus on tehty "Kehittynyt Android-Ohjelmointi"-kurssille (3 opintopistettä) vuonna 2023 kolmen muun henkilön kanssa. Ohjelmointikielenä käytettiin Kotlin:ia. Ohjelman ajaminen onnistuu Android Studio-ohjelman emulaattorilla tai omalla puhelimella, jonka on liittänyt em. ohjelmaan (testattu Android versiolla 10 sekä 11).

Ohjelma vaatii käyttäjän rekisteröinnin. Tunnus tallennetaan Googlen Firebase-tietokantaan, joka tätä kirjoittaessani (23.11.2023) on vielä pystyssä. Tunnuksen luonnissa kysytään sähköpostiosoite, käyttäjätunnus sekä salasana.

![image](https://github.com/miikkaniska/memoapp/assets/78212251/873d9489-e909-4381-ba77-dc906d38d5f0)

Kirjauduttuaan sisään käyttäjä voi luoda uuden muistion "create memo" nappulasta. Muistiolle on pakko antaa aina nimi. Käyttäjä voi päättää tekeekö muistiosta yksityisen, jolloin se näkyy vain itselleen, vai haluaako hän jakaa sen jonkun muun kanssa. Jälkimmäisessä vaihtoehdossa aukeaa alas tekstikenttä, johon tulee syöttää haluttujen henkilöiden nimimerkit.

![image](https://github.com/miikkaniska/memoapp/assets/78212251/5e3a0161-2ce6-4025-85a2-405ab6391aa2)

Muistio tulee näkyville päänäkymään, josta sen voi myös avata. Uudessa näkymässä näkyy milloin muistio on luotu ja kenen kanssa se on jaettu, jos on. käyttäjät voivat muokata tekstiä ja tallentaa muutokset painettuaan "save changes." Vain muistion luonut henkilö voi poistaa halutessaan muistion, muilla henkilöillä on mahdollisuus vain muokata muistion sisältöä.

![image](https://github.com/miikkaniska/memoapp/assets/78212251/adb20b5d-7eed-415d-bc93-6810fb0bdb36)
