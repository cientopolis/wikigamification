# wikigamification
Code to simulate the integration of Metagame project with Wikipedia production

To obtain the dataset use the Wikicrawler defined in the folder "R".

To generate the events of metagame:
1. Generate the database of Revision Machine following the instructions defined in [Creating the Revision Database](https://dkpro.github.io/dkpro-jwpl/RevisionMachine/), the packages are in Wikigamificator.
2. Run App defining the main folder with the output of Wikicrawler.
3. The output will be in the Wikicraler's output folder, It will be call evets.csv

To charge the events.csv to metagame:
1. Initiate Metagame with the seeds defined in Metagame/ .
2. Copy historical_badges.rake in the metagame lib/tasks .
3. And run the following command with the path to events.csv
	`rake historical_badges:create["<YOUR-PATH>/events.csv"] `
