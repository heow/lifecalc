# Life Calc - a tool for your ages

*"Train yourself to let go of everything you fear to lose."*  
- Yoda

Ever wonder how much time has past and how much you have remaining?  This is the tool for you.

### Prerequisites

Install [Babashka](https://github.com/babashka/babashka#installation) don't worry, it's easy.

### Usage

If you are _Borkdude_ just run it as is.

    $ ./lifecalc.clj 
    Borkdude is 14663 days old, expiration in 14959 days. (50.50% remaining)

Otherwise feed it your information.  Lifespan is in years, [look yours up here](https://en.wikipedia.org/wiki/List_of_countries_by_life_expectancy).

    $ ./lifecalc.clj --name Hubert --lifespan 75.5 -b 1969-03-26
    Hubert is 19000 days old, expiration in 8576 days. (31.10% remaining)

It also suports environment variables in the following format.  You can use the export feature to have it generate the variable for you.

    $ ./lifecalc.clj --help
      -n, --name NAME            Name of person
      -l, --lifespan YEARS       Lifespan in years
      -b, --birthday YYYY-MM-DD  Birthday
      -s, --stdin                Read or pipe in from STDIN
      -e, --export               EXPORT state to bash
      -h, --help

    $ ./lifecalc.clj --export --name Hubert --lifespan 75.5 -b 1969-03-26
    export LIFECALC='{:name "Hubert", :lifespan 75.5, :birthday "1969-03-26"}'

Feed the environment variable (or any combination of environment and command-line) in any way you want.

    $ LIFECALC='{:name "Hubert", :lifespan 75.5, :birthday "1969-03-26"}' ./lifecalc.clj 
    Hubert is 19000 days old, expiration in 8576 days. (31.10% remaining)
    
    $ export LIFECALC='{:name "Hubert", :lifespan 75.5, :birthday "1969-03-26"}'
    $ ./lifecalc.clj 
    Hubert is 19000 days old, expiration in 8576 days. (31.10% remaining)

You can use it like it is for a daily reminder to appreciate life.

# Advanced Usage - Conversions

Lifecalc will convert a calendar date to your personal day.

    $ ./lifecalc.clj 2021-04-01
    14662

...and vice-versa

    $ ./lifecalc.clj 14662
    2021-04-01

Or handily a percentage

    $ ./lifecalc.clj 50%
    14811

# Advanced Usage - Stdin

Lifecalc can read from stdin using the _-s_ or _-stdin_ options.  So given a file like this

    $ cat data-calendar-dates.txt 
    1969-07-20 moon landing
    1981-02-08 Borkdude born
    1989-11-09 Berlin Wall is taken down
    1992-06-15 US Vice-President Dan Quayle can't spell "potato"
    1998-06-01 Digital Equpiment Corporation (DEC) sold and dismantled
    2000-01-01 Y2K
    2001-11-16 Harry Potter and the Plilosophers Stone film released in theaters
    2007-10-16 Clojure release at LispNYC
    2008-09-28 SpaceX Falcon 1 launches to orbit
    2019-09-19 Babashka initial commit on Github
    2021-03-04 ABBA's 50th anniversary of "Waterloo"

You can get the peronal days of every calendar date.

    $ ./lifecalc.clj -s < data-calendar-dates.txt 
    -4221
    0
    3196
    4145
    6322
    6901
    7586
    9746
    10094
    14102
    14634

To add more perspecitve, included is a small program _interleave-with.clk_ which interleaves standard-in with a file.  Thus you can go full circle complete with explanations.

    $ cat data-calendar-dates.txt  | ./lifecalc.clj -s | ./interleave-with.clj data-calendar-dates.txt 
    -4221 1969-07-20 moon landing
    0 1981-02-08 Borkdude born
    3196 1989-11-09 Berlin Wall is taken down
    4145 1992-06-15 US Vice-President Dan Quayle can't spell "potato"
    6322 1998-06-01 Digital Equpiment Corporation (DEC) sold and dismantled
    6901 2000-01-01 Y2K
    7586 2001-11-16 Harry Potter and the Plilosophers Stone film released in theaters
    9746 2007-10-16 Clojure release at LispNYC
    10094 2008-09-28 SpaceX Falcon 1 launches to orbit
    14102 2019-09-19 Babashka initial commit on Github
    14634 2021-03-04 ABBA's 50th anniversary of "Waterloo"

The same with day-counts, included as a handy file.

    $ cat data-daycounts.txt 
    1000 first kiloday!
    5000 5K-days
    10000 10K-days
    11000
    12000
    19000
...
    34000
    35000 35K-days
    35000
    36000
    36525 100 years old!
    
    
    $ cat data-daycounts.txt | lifecalc.clj -s | ./interleave-with.clj data-daycounts.txt 
    1983-11-05 1000 first kiloday!
    1994-10-18 5000 5K-days
    2008-06-26 10000 10K-days
    2011-03-23 11000
    2013-12-17 12000
    2016-09-12 13000
    2019-06-09 14000
    2022-03-05 15000 15K-days
    2024-11-29 16000
    2027-08-26 17000
    2030-05-22 18000
    2031-02-08 18262 50 years old!
    2033-02-15 19000
    2035-11-12 20000 20K-days
    2038-08-08 21000
    2041-05-04 22000
    2044-01-29 23000
    2046-10-25 24000
    2049-07-21 25000 25K-days
    2052-04-16 26000
    2055-01-11 27000
    2057-10-07 28000
    2060-07-03 29000
    2063-03-30 30000 30K-days
    2065-12-24 31000
    2068-09-19 32000
    2071-06-16 33000
    2074-03-12 34000
    2076-12-06 35000 35K-days
    2076-12-06 35000
    2079-09-02 36000
    2081-02-08 36525 100 years old!

The same with percentages:

    $ cat data-percent.txt 
    10%
    20%
...
    120%
    125%
    
    $ cat data-percent.txt | ./lifecalc.clj -s
    2962
    5924
...
    35546
    37027

Here is a fun trick, remember the converstions convert a percentage to day.  Calling it again converts the day to a calendar date.

    $ cat data-percent.txt | ./lifecalc.clj -s | ./lifecalc.clj -s
    1989-03-20
    1997-04-29
...
    2078-06-05
    2082-06-25

So you can go full circle:

    $ cat data-percent.txt | ./lifecalc.clj -s | ./lifecalc.clj -s | ./interleave-with.clj data-percent.txt 
    1989-03-20 10%
    1997-04-29 20%
    2001-05-19 25%
    2005-06-08 30%
    2013-07-18 40%
    2021-08-28 50%
    2029-10-07 60%
    2037-11-16 70%
    2045-12-26 80%
    2054-02-04 90%
    2062-03-17 100%
    2070-04-26 110%
    2078-06-05 120%
    2082-06-25 125%

It looks like Borkdude is going to die on St Patricks day in 2062.

# References

* [Life expectancy by country (Wikipedia)](https://en.wikipedia.org/wiki/List_of_countries_by_life_expectancy)
