<h2>Klasa BasketSplitter</h2>
Jej działanie opiera się na metodzie split, która umożliwia podział elementów koszyka tak, aby ilość dostępnych dostaw była najmniejsza, a każda dostawa miała maksymalnie dużo elementów.

Metoda split działa w następujący sposób:

- Odwraca mapę: "produkt": [opcje dostawy] -> "opcja dostawy": [produkty]
- Z dostępnych dostaw tworzy listę wszystkich możliwych podzbiorów tych dostaw posortowaną rosnąco według długości podzbiorów
- Dla każdego podzbioru sprawdza czy możliwa jest dostawa wszystkich podanych produktów
- Po wybraniu minimalnej liczby dostaw i konkretnych dostaw przyporządkowuje produkty do dostaw tak, żeby dostawy posiadały maksymalną możliwą liczbę produktów

<h2>Testy</h2>

<h3>Jednostkowe</h3>
Sprawdzają działanie metody split oraz metod używanych do implementacji metody split.

<h3>Wydajnościowe</h3>
Sprawdzają czy podane rozwiązanie działa wystarczająco szybko dla danych wejściowych.
Zadanie przewiduje maksymalnie 1000 produktów dostępnych oraz maksymalnie 100 produktów w obrębie danego koszyka.
W obydwu przypadkach zaimplementowane rozwiązanie działa poniżej 1 sekundy.
