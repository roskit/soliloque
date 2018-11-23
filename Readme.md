From composer Fabien Lévy [website](https://www.fabienlevy.net)

# Soliloque sur [X, X, X & X], Commentaries from a computer about a misunderstood concert
Work for computer solo, circa 15′, Commissioned by the Festival Inventionen, Berlin.
Dedicated to Ingrid Beirer, Folkmar Heim and Thomas Seelig.

## Presentation

(Program note of the Festival Inventionen 2002)

Is it possible compose a mini-composer ? SOLILOQUE about \[X, X, X et X\] (the X’s are to be replaced by the first names of the composers of the other works which are used as samples, for example Soliloque about Pierre, Wolfgang and Williams) is not an actual work, but more a meta-score that the computer generates in real-time from the analysis and extracts of other pieces played during the concert. This means that the generated work is different for every concert, not only because the materials constituting this mosaic originate from the sonorities retained from other pieces, but also because the organization of the mosaic is itself transformed according to the analysis of the samples.

The goal here is not for the listener to recognize extracts replicated from other works of the concert (the extracts, which are no longer than twenty seconds, are generated from a chord here, a signal there, etc..). It is rather to keep the spirit and the color of those extracts in order to generate a “score”, and then to use them as “instruments” to interpret this score. The goal of this project is also to generate a slightly different piece for each performance (or more precisely, a different interpretation of the same piece). Indeed, the style of the composition remains the same, my own, without any randomness. In other words, it is not the computer that interprets the concert, but the concert that interprets the work, Soliloque.

This project of a meta-work depending on the context has allowed me to further develop and abstract some compositional techniques that I was using in my instrumental music. These techniques are however transposed here upon the sonorous phenomenon. Indeed, those techniques operate more on acoustical and psycho-acoustical parameters of sound than on traditional parameters like rhythm, tone, or dynamics. Here, the techniques are elaborated not on tones but on sound objects, not on rhythm but on position of reading and on duration, not on intervals but on the speed of reading, not on nuances but on intensity, filtering and spaces.

I wrote this contextual meta-score entirely with the software SuperCollider, within the possibilities and constraints that this “instrument” proposes, and with the precious help of Thomas Seelig, musical assistant, Frederic Roskam for the complex porting on OSX, and of Thomas Noll for the mathematical calculation of the ‘Vuza canons’.



## Install and run Soliloque

### Installation
. Computer : Minimum PowerMac G4, 1 GHz or more, 1 GB memory, System OS 10.4 (intel or powerpc) or after
. Download Soliloque versionin clicking here (36Mb) and decompress it.
. Sound manager / drivers: Supercollider, the software behind Soliloque, can work with the default Apple sound manager installed on your Mac. This driver is however only stereo. Supercollider also works with many other sound cards (Motu, Digidesign/protools, etc..) if you want to run it in multichannel. Once these drivers are downloaded, place them in the right folder, and change the sound manager in the Apple preferences. Important: activate the multichannel soundcard BEFORE opening Soliloque.

### To run Soliloque about [X, X, X and X]
Once Soliloque opens, the main windows appears.
Possible known bugs: possible crash in case of too long path betweens the different elements. If you see any difficulty, simply put your sounds in a folder on the desktop, and everything not so far from the Soliloque software.

Soliloque about \[X, X, et X\] (the X’s are to be replaced by the first names of the composers of the other works which are used as samples, for example Soliloque about Pierre, Wolfgang and Williams), is a music piece generated from six samples which come from other pieces, for example from the pieces played previously during the same concert.

In order to start Soliloque, four steps need to be completed:

a) Choice of the number of channels
Soliloque works for 1 to 16 channels. By default, the number of channels is 2 (stereophony), that can be changed in typing the desired number in the related box and in pressing enter. Musically, four channels at least are better to obtain the complex space moves requested in the piece (in the premiere, Soliloque was played on 12 separate channels).

b) Upload and preparation of the six samples
. Upload with the [LOAD] red button six monophonic, 5 and 20 seconds long (in any case shorter than one minute), preferably musically interesting (so that the listener might occasionally recognize the samples when quoted in Soliloque about …), contrasted (in color and dynamic) samples. They can for instance be excerped from pieces played before in the same concert. When the right sound file is loaded, its sound wave appears in the window. By default, Soliloque puts any prepared sound in the folder [sounds].
. For each uploaded sound, click the black [PREPARE] button. Soliloque will then prepare the sound file (and the sound will automatically be heared during the preparation). Please wait until the end of the preparation before starting any new operation.
. Redo this operation for each sample.
Possible known bug: the first time you open Soliloque, the information “File ‘Soliloque.app/Contents/Resources/sounds/Sample1.wav’ could not be opened.” appears in the white windows. Don’t worry! Solliloque chooses per default the last opened sound before you choose a new one, and in this case, doesn’t find anything.

c) Choice of strategic points by manual or automatic analysis of the samples
. The computer needs three strategical points for each sample. They can be automatically or manually choosen. When automatically choosen by the computer, those points correspond to two maxima and a minimum of the sample.
. First push the black key [AUTO-ANALYSE] for an automatic analysis. Numbers will automatically appear within a few seconds in the related boxes [tmax], [freq], [amp].
. If you want to manually choose your own points, select them in the sound wave window, click the buttons \[Set tmax1\] (for the position tmax1), \[Set tmax2\] (for the position tmax2) or \[Set tmin\] (for the position tmin). The related values will appear in the corresponding boxes. You can always hear your choice or a region of the sound in selecting a region and pushing the button [PLAY].
. Pushing the [SAVE] button will save all your choices: samples, analysis, and the chosen positions for the next opening of Soliloque (therefore, the result won’t be affected if you close the interface).
. Click now on [Soliloque go !] to play your realisation of Soliloque.
Possible known bug: be careful to have no ZERO in the analysis (it will crash Soliloque). If you have a zero, it generally means that more than the half of your sound is silent. In this case, choose manually three points where there is a sound.

-> Musical tip 1: we noticed that Soliloque “works” better when particular points are manually chosen: precise, contrasted, semantically consistent, colored points. Also, the form “works” better if the strongest points (Tmx and Tmax) are really energetic, and if Tmin is softer (they are more contrasts in the piece).

d) Some tips for the interpretation of Soliloque about [X, X et X]
. Soliloque is a work with space’s effects. This means that it is musically better to play Soliloque with many channels (4 to 16 channels). The space movements and amplitude of each sound are controlled very precisely by the computer.
. we also advise a light general diffusion of the piece with a mix table in order to increase the contrasts and the “human qualities” of the piece, and a light reverberation of the entire piece.
. Soliloque works much better in the dark or almost without light (for concentration purposes).

## Protection and copyright of Soliloque about [X, X, X and X]
Soliloque about [X, X, X..] is a freeware . It can be freely played for public or private audience. We only request concert organisers to respect the construction of the title of the piece (Soliloque about [X, X, et X], where the X are the first name of the composers of the samples), to aknowledge the name of the authors (Fabien Lévy -composer-; Thomas Seelig and Frederic Roskam – musical assistants), and to quote the program notes written above. This work is protected by the SACEM (French society for copyright). We also would be happy to be informed about any concert programing Soliloque and to receive a record of your version of Soliloque.

---

# Soliloque sur [X, X, X & X], Commentaire par un ordinateur d’un concert mal compris de lui

## Présentation

(Texte de programme du Festival Inventionen 2002)

Composer un mini-compositeur. SOLILOQUE sur [X, X, X et X] n’est pas à proprement parlé une oeuvre, mais plutôt une méta-partition que l’ordinateur génère en temps réel à partir de l’analyse et d’extraits des autres pièces du concert. Ceci signifie qu’à chaque concert, l’oeuvre engendrée est différente, non seulement parce que le matériau constituant cette mosaïque est fait du souvenir des sonorités des autres pièces, mais aussi parce que l’organisation de cette mosaïque est elle-même transformée en fonction de l’analyse des échantillons.

Il ne s’agit pas ici que l’on cite et que l’on reconnaisse les autres pièces du concert (les extraits utilisés, qui ne dépassent pas vingt secondes, sont constitués d’un accord ici, d’un signal instrumental là, d’une formule courte que l’on peut à la rigueur reconnaître). Il s’agit plutôt de garder l’esprit et la couleur de ces extraits, d’en tirer par l’analyse une partition, puis de les utiliser de façon détournée comme “instruments” jouant l’oeuvre. Il ne s’agit pas non plus d’engendrer à chaque fois une pièce différente. Le langage reste le même, le mien, sans aléatoire, et la partition est plutôt « interpretée » par le contexte des autres pièces. En d’autres termes, ce n’est pas tant ici l’ordinateur qui interprète un concert, mais le concert qui interprète une oeuvre.

Ce projet d’une méta-oeuvre dépendant du contexte m’a permis de développer plus avant et de façon abstraite les techniques de composition que j’utilisais auparavant dans mes oeuvres instrumentales, issues non d’une grammatologie abstraite sur le signe, mais de la réalité du phénomène sonore. En effet, ces techniques n’opèrent pas sur les paramètres classiques de rythme, de hauteur, et de nuances, issus de notre façon d’écrire la musique en Occident, mais sur des paramètres acoustiques et électroacoustiques du son : ici, il s’agit d’élaborer des techniques de composition non pas sur des hauteurs mais sur des objets sonores, non pas sur des rythmes mais sur des positions de lecture et de durée, non pas sur des intervalles mais sur des vitesses de lecture, non pas sur des nuances mais sur des intensités, des filtrages, et des espaces.

Ce projet de méta-partition contextuelle a été entièrement programmé sur le logiciel Super Collider, avec les possibilités et les contraintes que propose cet « instrument », et avec l’aide précieuse de Thomas Seelig, assistant musical, Frédéric Roskam, pour le portage difficile de OS9 à OSX, et de Thomas Noll, pour le calcul mathématique des canons de Vuza.

## Installation et démarrage

### Installation

* Ordinateur : Minimum PowerMac G4, 1 GHz ou plus, 1 GB mémoire, System OS 10.4 (intel or powerpc) ou supérieur
* Télécharger l’application Soliloque en cliquant ici (34 Mo) et la décompresser.
* Carte son / drivers: Supercollider OSX (le logiciel utilisé derrière Solliloque) peut fonctionner avec la carte son installée par défaut sur les ordinateurs Apple (Apple sound manager) mais celle-ci n’est que stéréo. Supercollider fonctionne également avec d’autres cartes son (Motu, Digidesign/protools, etc..). Il faut alors télécharger les drivers nécessaires, puis changer dans les préférences Apple le sound manager souhaité.
Important: il faut activer la carte son multicanal AVANT de lancer Soliloque.

### Démarrer Soliloque sur [X, X, X and X]
L’ouverture de Soliloque fait apparaitre la fenêtre suivante :
[cml_media_alt id='3804']EcranSol[/cml_media_alt]

Bug connu possible: Soliloque peut parfois se bloquer si le chemin du fichier est trop long (dossier de dossier de dossier etc..). En cas de bloquage, laissez les sons dans un dossier sur le desktop et réessayez.

Soliloque sur \[X, X, et X\] (mettre à la place des X les prénoms des compositeurs des autres pièces servant d’échantillon, par exemple Soliloque sur Pierre, Wolfgang et William), est une pièce générée à partir de six échantillons provenant d’autres pièces, par exemple d’autres pièces jouées auparavant dans un concert.

Afin de démarrer Soliloque, quatre étapes seront nécessaires :

a) Nombre de canaux
Soliloque se joue dans l’espace, sur 1 à 16 canaux. Indiquez le nombre de canaux utilisés dans la case désignée et appuyez sur Enter. Par défaut, le nombre de canaux est 2 (stéréophonie), mais il est vivement conseillé en concert une réalisation à quatre canaux ou plus (la création fut pour 12 canaux).

b) Chargement et préparation des six échantillons
* Enregistrez avec le bouton [LOAD] six échantillons monophoniques, d’une durée de 5 à 20 secondes (maximum 60 secondes), de préférence intéressants musicalement (afin que l’auditeur puisse reconnaitre occasionnellement l’échantillon quand celui-ci est cité), contrastés (en couleur et dynamique). Il peut par exemple s’agir des pièces jouées auparavant dans le même concert. Lorsque le son est chargé par l’ordinateur, sa forme d’onde apparaît dans la fenètre. Par défaut, Soliloque enregistre les sons dans le dossier [sounds].
* Pour chaque son chargé, cliquer sur la touche noire [PREPARE]. Soliloque preparera alors le fichier son (que vous entendrez pendant la préparation). Attendez jusqu’à la fin de la préparation avant d’entamer une nouvelle opération.
* Répéter cette opération pour chacun des six échantillons monophoniques.

Bug connu possible: à la première ouverture de Soliloque, la phrase « File ‘Soliloque.app/Contents/Resources/sounds/Sample1.wav’ could not be opened. » peut apparaitre dans la fenètre du listener. Pas de panique! Soliloque choisit par défaut le son ouvert à la précédente ouverture, jusqu’à ce que vous choisissiez un nouveau son. Dans ce cas, il n’en trouve aucun.

c) Choix manuels ou automatiques de trois points stratégiques
* L’ordinateur doit choisir trois points particuliers par échantillon. Dans les faits, ces points correspondent à deux maxima et un minimum de l’échantillon, lorsqu’ils sont choisis automatiquement.
* Cliquer sur le bouton noir [AUTO-ANALYSE] pour une analyse automatique. Les valeurs de l’analyse des trois points apparaissent automatiquement dans les cadres [tmax], [freq], [amp].
* Si vous préféréz choisir manuellement trois points qui vous semblent critiques, sélectionnez-les dans la fenètre de forme d’onde, et cliquez sur les boutons \[Set tmax1\] (pour la position tmax1), \[Set tmax2\] (pour la position tmax2) ou \[Set tmin\] (pour la position tmin). Les valeurs apparaissent dans les cadres correspondants. Vous pouvez écoutez auparavant vos choix et une région particulière avec la sélection d’une fenètre et le bouton [PLAY].
* le bouton [SAVE] enregistrent toutes les décisions: échantillons choisis, analyse, et points particuliers. Ainsi, la fermeture puis réouverture de l’interface de Soliloque n’affecte pas la pièce réalisée.
* Cliquer sur [Soliloque go !] pour jouer cette réalisation de Soliloque.

Bug connu: vérifier que l’analyse ne génère aucune valeur zéro, ce qui « crasherait » Soliloque dès le démarage. Une valeur zéro signifie généralement que le fichier son est pour plus de sa moitié du silence. Dans ce cas, sélectionnez manuellement des points non silencieux.

- Conseil musical : la pièce « fonctionne » mieux lorsque des points particuliers sont choisis à la main : des points acoustiquement clairs, différenciés, sémantiquement signifiants. De plus, la forme « fonctionne » mieux si les points « forts » (Tmax1 et Tmax2) sont réellement énergétiques, et le Tmin doux (contrastes plus présents). Cependant, toutes les expériences musicales restent possibles.

d) conseils d’interprétation
. La spatialisation et les volumes étant gérés de façon automatique par l’ordinateur, nous conseillons une légère diffusion, à la table de mixage, de l’ensemble afin de la rendre plus « humaine » et plus « exagérée ». La partition de Soliloque est aussi une écriture de l’espace. Si Soliloque peut éventuellement être interprétée en stéréophonie, il est vivement conseillé en concert de multiplier les canaux (4 ou 8 pour un effet musical acceptable; la création s’était faite sur 12 canaux).
. Nous conseillons également une très légère réverbération de l’ensemble de la pièce.
. Nous conseillons de plonger la salle de concert dans la pénombre afin de favoriser l’écoute de cette pièce sans interprète sur scène.

## Protection et droits d’auteur de Soliloque sur [X, X, X et X]
Soliloque sur [X, X, X..] est en libre accès sur internet, et peut être librement jouée en public comme en exécution privée. Il est cependant demandé de préciser les auteurs de l’oeuvre dans le programme : Fabien Lévy (compositeur), Frédéric Roskam et Thomas Seelig (assistants musicaux), au besoin de reprendre la note de programme ci-dessus, et de respecter la construction du titre (Soliloque sur [X, X, et X], les X étant a priori les prénoms des compositeurs des échantillons). Il est évidemment demandé, en cas d’éxécution publique, de déclarer l’interprétation de l’oeuvre et son compositeur (Fabien Lévy) à la société de droit d’auteur de son pays. L’oeuvre est déposée et protégée à la SACEM (société française de protection des droits d’auteurs). Nous demandons aussi, en échange, de nous avertir et, dans la mesure du possible, de nous envoyer un enregistrement de cette version.
