# InformationRetrieval
Given posting lists generated from the RCV1 news corpus (http://www.daviddlewis.com/resources/testcollections/rcv1/). Rebuilding the index after reading in the data. Linked List is used to store the index data in memory in a specific format. Constructed two index with two different ordering strategies: with one strategy, the posting of each term is ordered by increasing document IDs; with the other strategy, the postings of each term is ordered by decreasing term frequencies. Then, implemented modules that return documents based on term-at-a-time with the postings list ordered by term frequencies, and document-at-a-time with the postings list ordered by doc IDs for a set of queries.
