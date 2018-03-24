## svaap - Sivuvieritysavaruusammuntapeli

teksti generoitu: https://fontmeme.com/futuristic-fonts/

### ssh tunnelointi servuun yhdistämiseen metropolian verkon ulkopuolelta
    ssh -L 2280:10.114.34.61:3306 -N <ktunnus>@shell.metropolia.fi
```<ktunnus>``` on metropolian käyttäjätunnus. Komento täytyy jättää taustalle päälle, jotta se ohjaa ajonaikaset porttiin ```2280``` menevät jutskat metropolian shellin kautta meidän servun tietokantaan, joka kuuntelee porttia ```3306```.
