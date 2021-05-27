![ezgif com-gif-maker (1)](https://user-images.githubusercontent.com/72752940/119895272-defd8900-bf3d-11eb-9fe1-eba8877ab4b5.gif)


Projektujemy symulacje agentową, w której będą występować trzy podstawowe rodzaje agentów: ptak, drapieżny ptak, przeszkoda. Symulacja ma przedstawić sposób poruszania się stada ptaków, ich ucieczki przed drapieżnikami oraz unikania przez nie przeszkód. Nasza symulacja jest animacją dwuwymiarową wizualizowaną na ekranie w czasie rzeczywistym.

Zachowanie ptaków:

• każdy ptak będzie musiał trzymać pewną odległość od innych w stadzie. Na podstawie tej reguły ptak będzie dążył do pozycji opisanej jako suma wektorów różnicy pozycji między rozważanym ptakiem a pozycją reszty ptaków znajdujący się w jego polu widzenia.

• każdy ptak będzie dążył do bliskości z innymi ptakami, aby stado się nie rozpadło. Na podstawie tej reguły ptak będzie dążył do środka masy ptaków w polu widzenia, czyli średniej arytmetycznej pozycji ptaków.

• każdy ptak będzie dostosowywał swój kierunek ruchu do innych ptaków. Na podstawie tej reguły prędkość ptaka będzie opisana jako średnia arytmetyczna prędkości pozostałych ptaków w polu widzenia.

• każdy ptak będzie unikał Drapieżników. Kiedy Ptak zobaczy Drapieżnika w swoim polu widzenia, będzie się poruszał w kierunku przeciwnym do kierunku ruchu Drapieżnika tym szybciej, im bliżej będzie drapieżnik. Ptaki, będą miały prędkość maksymalną, większą od drapieżników, co umożliwi im ucieczkę.

• Każdy ptak będzie unikał przeszkód. Kiedy ptak zobaczy w swoim polu widzenia przeszkodę, będzie podążał za promieniem swojego pola widzenia, najbliższego do kierunku lotu, takiego co nie przecina się z przeszkodą.

Zachowanie drapieżnych ptaków:

• Drapieżne ptaki będą miały za zadanie gonić zwykłe. Jeśli tylko zobaczą ptaki w swoim polu widzenia będą poruszać się w kierunku ich środka masy.

• każdy drapieżny ptak będzie musiał trzymać pewną odległość od innych. Na podstawie tej reguły ptak będzie dążył do pozycji opisanej jako suma wektorów różnicy pozycji między rozważanym ptakiem a pozycją reszty ptaków znajdujący się w jego polu widzenia.

• Każdy drapieżny ptak będzie unikał przeszkód. Kiedy ptak zobaczy w swoim polu widzenia przeszkodę, będzie podążał za promieniem swojego pola widzenia, najbliższego do kierunku lotu, takiego co nie przecina się z przeszkodą.

Pola widzenia obu rodzaju ptaków będą miały charakter promieni, które będą zawierać się w kształcie wycinka pola o promieniu i kącie zdefiniowanym przez użytkownika. Ptaki będą znajdowały się w polu widzenia, kiedy ich pozycja znajdzie się w wycinku koła pola widzenia. Przeszkoda będzie znajdowała się w polu widzenia, jeżeli jej krawędź w tym polu będzie przecinała się z promieniem pola widzenia. Program będzie w trakcie animacji zapisywał do pliku średnie wartości prędkości Ptaków i osobno Drapieżników.
