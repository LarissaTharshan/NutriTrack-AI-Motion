# NutriTrack AI & Motion

## Bearbeitet von
* Larissa Tharshan

## Herausstellungsmerkmal

**Dynamischer Protein-Rezeptskalierer**

Der zentrale Mehrwert der App ist der mathematisch implementierte `RecipeScalerUseCase`:
Alle Zutatenmengen von High-Protein-Rezepten werden automatisch auf das individuelle
Protein-Tagesziel des Users skaliert (scaleFactor = userProteinGoal / baseRecipeProtein).
Dies ist keine reine UI-Anpassung, sondern eine echte Domain-Logik mit vollständiger
Unit-Test-Abdeckung.

## Getting Started

1. **Profil einrichten:** Name eingeben, Avatar aus der Fitness-Galerie wählen
2. **Tagesziele setzen:** Kalorien, Protein, Kohlenhydrate, Fett definieren und speichern
3. **Dashboard:** Übersicht der gesetzten Ziele
4. **Lebensmittel suchen:** OpenFoodFacts REST-API — Makronährstoffe abrufen
5. **Rezept-Skalierer:** Rezept wählen → Zutaten werden auf dein Protein-Ziel skaliert

## Technische Umsetzung

* **Architektur:** MVVM (Data Layer → ViewModel → UI Layer)
* **UI:** Vollständig in Jetpack Compose implementiert
* **REST-API:** OpenFoodFacts API via Retrofit + kotlinx.serialization
* **Lokaler Storage:** Preferences DataStore (gerätespezifisch, kein Server)
* **Navigation:** Jetpack Navigation Compose (4 Screens)
* **Tests:** Unit Tests (RecipeScalerTest) + Logic Tests (NutriTrackUITest)

## Screens

| Screen | Beschreibung |
|--------|-------------|
| ProfileScreen | Name, Avatar-Auswahl, Tagesziele (DataStore) |
| DashboardScreen | Ziel-Übersicht, Navigation |
| SearchScreen | OpenFoodFacts Suche mit Loading/Error/Empty State |
| RecipeScreen | Protein-Rezeptskalierer (Alleinstellungsmerkmal) |

## Abgabe
Freitag, 05.06.2026, 14:00 Uhr