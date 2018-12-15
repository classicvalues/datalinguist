(ns computerese.annotations
  (:import [edu.stanford.nlp.pipeline]
           [edu.stanford.nlp.util TypesafeMap]
           [edu.stanford.nlp.ling CoreAnnotations$TextAnnotation
                                  CoreAnnotations$LemmaAnnotation
                                  CoreAnnotations$PartOfSpeechAnnotation
                                  CoreAnnotations$NamedEntityTagAnnotation
                                  CoreAnnotations$SentencesAnnotation
                                  CoreAnnotations$TokensAnnotation
                                  CoreAnnotations$NamedEntityTagAnnotation
                                  CoreAnnotations$BeforeAnnotation
                                  CoreAnnotations$AfterAnnotation
                                  CoreAnnotations$IndexAnnotation
                                  CoreAnnotations$SentenceIndexAnnotation
                                  CoreAnnotations$CharacterOffsetBeginAnnotation
                                  CoreAnnotations$CharacterOffsetEndAnnotation]
           [edu.stanford.nlp.semgraph SemanticGraphCoreAnnotations$BasicDependenciesAnnotation
                                      SemanticGraphCoreAnnotations$EnhancedDependenciesAnnotation
                                      SemanticGraphCoreAnnotations$EnhancedPlusPlusDependenciesAnnotation]))

;;;; This namespace contains convenience functions for accessing the most common annotations of Stanford CoreNLP.
;;;; The functions are designed to be chained using the ->> macro or through function composition.
;;;; Please note that *any* annotation can be accessed using corenlp-clj.annotations/annotation,
;;;; you are not just limited to using the convenience functions provided in this namespace.
;;;;
;;;; The functions here mirror the annotation system of Stanford CoreNLP: once the returned object is not a TypesafeMap
;;;; or a seq of TypesafeMap objects, annotation functions cannot retrieve anything from it.
;;;; An example of this might be the dependency-graph annotation which returns a SemanticGraph object.
;;;; However, using a function such as corenlp-clj.semgraph/nodes on a SemanticGraph object returns IndexedWord objects
;;;; which *are* implementations of TypesafeMap. Consequently, the annotation functions can take them as params.
;;;;
;;;; As a general rule, functions with names that are pluralised have a seqable output, e.g. sentences or tokens.
;;;; This does not matter when chaining these functions, as all annotation functions will also implicitly map to seqs.

(defn annotation
  "Access the annotation of x as specified by class.
  x may also be a seq of objects carrying annotations."
  [^Class class x]
  (if (seqable? x)
    (map (partial annotation class) x)
    (.get ^TypesafeMap x class)))

(defn text "The text of x (TextAnnotation)." [x]
  (annotation CoreAnnotations$TextAnnotation x))

(defn lemma "The lemma of x (LemmaAnnotation)." [x]
  (annotation CoreAnnotations$LemmaAnnotation x))

(defn pos "The part-of-speech of x (PartOfSpeechAnnotation)." [x]
  (annotation CoreAnnotations$PartOfSpeechAnnotation x))

(defn ner "The named entity tag of x (NamedEntityTagAnnotation)." [x]
  (annotation CoreAnnotations$NamedEntityTagAnnotation x))

(defn sentences "The sentences of x (SentencesAnnotation)." [x]
  (annotation CoreAnnotations$SentencesAnnotation x))

(defn tokens "The tokens of x (TokensAnnotation)." [x]
  (annotation CoreAnnotations$TokensAnnotation x))

(defn offset
  "The character offset of x (CharacterOffsetBeginAnnotation -or- CharacterOffsetEndAnnotation).
  Style can be :begin (default) or :end."
  ([style x]
   (case style
     :begin (annotation CoreAnnotations$CharacterOffsetBeginAnnotation x)
     :end (annotation CoreAnnotations$CharacterOffsetEndAnnotation x)))
  ([x]
   (offset :begin x)))

(defn index
  "The index of x (IndexAnnotation -or- SentenceIndexAnnotation).
  Style can be :token (default) or :sentence."
  ([style x]
   (case style
     :token (annotation CoreAnnotations$IndexAnnotation x)
     :sentence (annotation CoreAnnotations$SentenceIndexAnnotation x)))
  ([x]
   (index :token x)))

(defn whitespace
  "The whitespace around x (BeforeAnnotation -or- AfterAnnotation).
  Style can be :before (default) or :after."
  ([style x]
   (case style
     :before (annotation CoreAnnotations$BeforeAnnotation x)
     :after (annotation CoreAnnotations$AfterAnnotation x)))
  ([x]
   (whitespace :before x)))

(defn dependency-graph
  "The dependency graph of x (BasicDependenciesAnnotation -or-
  EnhancedDependenciesAnnotation -or- EnhancedPlusPlusDependenciesAnnotation).
  Style can be :basic, :enhanced or :enhanced++ (default)."
  ([style x]
   (case style
     :basic (annotation SemanticGraphCoreAnnotations$BasicDependenciesAnnotation x)
     :enhanced (annotation SemanticGraphCoreAnnotations$EnhancedDependenciesAnnotation x)
     :enhanced++ (annotation SemanticGraphCoreAnnotations$EnhancedPlusPlusDependenciesAnnotation x)))
  ([x]
   (dependency-graph :enhanced++ x)))