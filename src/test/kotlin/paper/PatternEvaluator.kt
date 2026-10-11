package paper

import org.junit.jupiter.api.DisplayName

/**
 * Example implementation of PatternEvaluatorBase for testing pattern evaluation.
 *
 * To use this:
 * 1. Create a master CSV file with columns: Applicable?, ID, Exp. Result, MTL
 * 2. Create data files named: "masterfile"_data_"testid".csv
 * 3. Create MTL files named: "masterfile"_"mtlid".mtl
 * 4. Update MASTER_CSV_PATH to point to your master CSV file
 * 5. Implement the executeTest method with your actual test logic
 */
@DisplayName("Pattern Evaluator Tests")
// Absence

class AbsenceGloballyEvaluator : PatternEvaluatorBase("src/test/resources/absence-globally/absence-globally.csv")

class AbsenceBeforeEvaluator : PatternEvaluatorBase("src/test/resources/absence-before/absence-before.csv")

class AbsenceAfterEvaluator : PatternEvaluatorBase("src/test/resources/absence-after/absence-after.csv")

class AbsenceBetweenEvaluator : PatternEvaluatorBase("src/test/resources/absence-between/absence-between.csv")

class AbsenceUntilEvaluator : PatternEvaluatorBase("src/test/resources/absence-until/absence-until.csv")

// Existence

class ExistenceGloballyEvaluator : PatternEvaluatorBase("src/test/resources/existence-globally/existence-globally.csv")

class ExistenceBeforeEvaluator : PatternEvaluatorBase("src/test/resources/existence-before/existence-before.csv")

class ExistenceAfterEvaluator : PatternEvaluatorBase("src/test/resources/existence-after/existence-after.csv")

class ExistenceBetweenEvaluator : PatternEvaluatorBase("src/test/resources/existence-between/existence-between.csv")

class ExistenceUntilEvaluator : PatternEvaluatorBase("src/test/resources/existence-until/existence-until.csv")

// Universality

class UniversalityGloballyEvaluator : PatternEvaluatorBase("src/test/resources/universality-globally/universality-globally.csv")

class UniversalityBeforeEvaluator : PatternEvaluatorBase("src/test/resources/universality-before/universality-before.csv")

class UniversalityAfterEvaluator : PatternEvaluatorBase("src/test/resources/universality-after/universality-after.csv")

class UniversalityBetweenEvaluator : PatternEvaluatorBase("src/test/resources/universality-between/universality-between.csv")

class UniversalityUntilEvaluator : PatternEvaluatorBase("src/test/resources/universality-until/universality-until.csv")

// Bounded Existence

class BoundedExistenceGloballyEvaluator : PatternEvaluatorBase("src/test/resources/bounded-existence-globally/bounded-existence-globally.csv")

class BoundedExistenceBeforeEvaluator : PatternEvaluatorBase("src/test/resources/bounded-existence-before/bounded-existence-before.csv")

class BoundedExistenceAfterEvaluator : PatternEvaluatorBase("src/test/resources/bounded-existence-after/bounded-existence-after.csv")

class BoundedExistenceBetweenEvaluator : PatternEvaluatorBase("src/test/resources/bounded-existence-between/bounded-existence-between.csv")

class BoundedExistenceUntilEvaluator : PatternEvaluatorBase("src/test/resources/bounded-existence-until/bounded-existence-until.csv")

// Recurrence

class RecurrenceGloballyEvaluator : PatternEvaluatorBase("src/test/resources/recurrence-globally/recurrence-globally.csv")

class RecurrenceBeforeEvaluator : PatternEvaluatorBase("src/test/resources/recurrence-before/recurrence-before.csv")

class RecurrenceAfterEvaluator : PatternEvaluatorBase("src/test/resources/recurrence-after/recurrence-after.csv")

class RecurrenceBetweenEvaluator : PatternEvaluatorBase("src/test/resources/recurrence-between/recurrence-between.csv")

class RecurrenceUntilEvaluator : PatternEvaluatorBase("src/test/resources/recurrence-until/recurrence-until.csv")

// Minimum Duration

class MinimumDurationGloballyEvaluator : PatternEvaluatorBase("src/test/resources/minimum-duration-globally/minimum-duration-globally.csv")

class MinimumDurationBeforeEvaluator : PatternEvaluatorBase("src/test/resources/minimum-duration-before/minimum-duration-before.csv")

class MinimumDurationAfterEvaluator : PatternEvaluatorBase("src/test/resources/minimum-duration-after/minimum-duration-after.csv")

class MinimumDurationBetweenEvaluator : PatternEvaluatorBase("src/test/resources/minimum-duration-between/minimum-duration-between.csv")

class MinimumDurationUntilEvaluator : PatternEvaluatorBase("src/test/resources/minimum-duration-until/minimum-duration-until.csv")

// Maximum Duration

class MaximumDurationGloballyEvaluator : PatternEvaluatorBase("src/test/resources/maximum-duration-globally/maximum-duration-globally.csv")

class MaximumDurationBeforeEvaluator : PatternEvaluatorBase("src/test/resources/maximum-duration-before/maximum-duration-before.csv")

class MaximumDurationAfterEvaluator : PatternEvaluatorBase("src/test/resources/maximum-duration-after/maximum-duration-after.csv")

class MaximumDurationBetweenEvaluator : PatternEvaluatorBase("src/test/resources/maximum-duration-between/maximum-duration-between.csv")

class MaximumDurationUntilEvaluator : PatternEvaluatorBase("src/test/resources/maximum-duration-until/maximum-duration-until.csv")

// Response

class ResponseGloballyEvaluator : PatternEvaluatorBase("src/test/resources/response-globally/response-globally.csv")

class ResponseBeforeEvaluator : PatternEvaluatorBase("src/test/resources/response-before/response-before.csv")

class ResponseAfterEvaluator : PatternEvaluatorBase("src/test/resources/response-after/response-after.csv")

class ResponseBetweenEvaluator : PatternEvaluatorBase("src/test/resources/response-between/response-between.csv")

class ResponseUntilEvaluator : PatternEvaluatorBase("src/test/resources/response-until/response-until.csv")


class ResponseConstraintGloballyEvaluator : PatternEvaluatorBase("src/test/resources/response-constraint-globally/response-constraint-globally.csv")

class ResponseConstraintBeforeEvaluator : PatternEvaluatorBase("src/test/resources/response-constraint-before/response-constraint-before.csv")

class ResponseConstraintAfterEvaluator : PatternEvaluatorBase("src/test/resources/response-constraint-after/response-constraint-after.csv")

class ResponseConstraintBetweenEvaluator : PatternEvaluatorBase("src/test/resources/response-constraint-between/response-constraint-between.csv")

class ResponseConstraintUntilEvaluator : PatternEvaluatorBase("src/test/resources/response-constraint-until/response-constraint-until.csv")

// Response 1N Chain

class Response1nGloballyEvaluator : PatternEvaluatorBase("src/test/resources/response-1n-globally/response-1n-globally.csv")

class Response1nBeforeEvaluator : PatternEvaluatorBase("src/test/resources/response-1n-before/response-1n-before.csv")

class Response1nAfterEvaluator : PatternEvaluatorBase("src/test/resources/response-1n-after/response-1n-after.csv")

class Response1nBetweenEvaluator : PatternEvaluatorBase("src/test/resources/response-1n-between/response-1n-between.csv")

class Response1nUntilEvaluator : PatternEvaluatorBase("src/test/resources/response-1n-until/response-1n-until.csv")


class Response1nConstraintGloballyEvaluator : PatternEvaluatorBase("src/test/resources/response-1n-constraint-globally/response-1n-constraint-globally.csv")

class Response1nConstraintBeforeEvaluator : PatternEvaluatorBase("src/test/resources/response-1n-constraint-before/response-1n-constraint-before.csv")

class Response1nConstraintAfterEvaluator : PatternEvaluatorBase("src/test/resources/response-1n-constraint-after/response-1n-constraint-after.csv")

class Response1nConstraintBetweenEvaluator : PatternEvaluatorBase("src/test/resources/response-1n-constraint-between/response-1n-constraint-between.csv")

class Response1nConstraintUntilEvaluator : PatternEvaluatorBase("src/test/resources/response-1n-constraint-until/response-1n-constraint-until.csv")

// Response N1 Chain

class Responsen1GloballyEvaluator : PatternEvaluatorBase("src/test/resources/response-n1-globally/response-n1-globally.csv")

class Responsen1BeforeEvaluator : PatternEvaluatorBase("src/test/resources/response-n1-before/response-n1-before.csv")

class Responsen1AfterEvaluator : PatternEvaluatorBase("src/test/resources/response-n1-after/response-n1-after.csv")

class Responsen1BetweenEvaluator : PatternEvaluatorBase("src/test/resources/response-n1-between/response-n1-between.csv")

class Responsen1UntilEvaluator : PatternEvaluatorBase("src/test/resources/response-n1-until/response-n1-until.csv")


class Responsen1ConstraintGloballyEvaluator : PatternEvaluatorBase("src/test/resources/response-n1-constraint-globally/response-n1-constraint-globally.csv")

class Responsen1ConstraintBeforeEvaluator : PatternEvaluatorBase("src/test/resources/response-n1-constraint-before/response-n1-constraint-before.csv")

class Responsen1ConstraintAfterEvaluator : PatternEvaluatorBase("src/test/resources/response-n1-constraint-after/response-n1-constraint-after.csv")

class Responsen1ConstraintBetweenEvaluator : PatternEvaluatorBase("src/test/resources/response-n1-constraint-between/response-n1-constraint-between.csv")

class Responsen1ConstraintUntilEvaluator : PatternEvaluatorBase("src/test/resources/response-n1-constraint-until/response-n1-constraint-until.csv")

// Relaxed Response 1N Chain

class RelaxedResponse1nGloballyEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-response-1n-globally/relaxed-response-1n-globally.csv")

class RelaxedResponse1nBeforeEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-response-1n-before/relaxed-response-1n-before.csv")

class RelaxedResponse1nAfterEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-response-1n-after/relaxed-response-1n-after.csv")

class RelaxedResponse1nBetweenEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-response-1n-between/relaxed-response-1n-between.csv")

class RelaxedResponse1nUntilEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-response-1n-until/relaxed-response-1n-until.csv")


class RelaxedResponse1nConstraintGloballyEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-response-1n-constraint-globally/relaxed-response-1n-constraint-globally.csv")

class RelaxedResponse1nConstraintBeforeEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-response-1n-constraint-before/relaxed-response-1n-constraint-before.csv")

class RelaxedResponse1nConstraintAfterEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-response-1n-constraint-after/relaxed-response-1n-constraint-after.csv")

class RelaxedResponse1nConstraintBetweenEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-response-1n-constraint-between/relaxed-response-1n-constraint-between.csv")

class RelaxedResponse1nConstraintUntilEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-response-1n-constraint-until/relaxed-response-1n-constraint-until.csv")

// Relaxed Response N1 Chain

class RelaxedResponsen1GloballyEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-response-n1-globally/relaxed-response-n1-globally.csv")

class RelaxedResponsen1BeforeEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-response-n1-before/relaxed-response-n1-before.csv")

class RelaxedResponsen1AfterEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-response-n1-after/relaxed-response-n1-after.csv")

class RelaxedResponsen1BetweenEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-response-n1-between/relaxed-response-n1-between.csv")

class RelaxedResponsen1UntilEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-response-n1-until/relaxed-response-n1-until.csv")


class RelaxedResponsen1ConstraintGloballyEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-response-n1-constraint-globally/relaxed-response-n1-constraint-globally.csv")

class RelaxedResponsen1ConstraintBeforeEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-response-n1-constraint-before/relaxed-response-n1-constraint-before.csv")

class RelaxedResponsen1ConstraintAfterEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-response-n1-constraint-after/relaxed-response-n1-constraint-after.csv")

class RelaxedResponsen1ConstraintBetweenEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-response-n1-constraint-between/relaxed-response-n1-constraint-between.csv")

class RelaxedResponsen1ConstraintUntilEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-response-n1-constraint-until/relaxed-response-n1-constraint-until.csv")

// Precedence

class PrecedenceGloballyEvaluator : PatternEvaluatorBase("src/test/resources/precedence-globally/precedence-globally.csv")

class PrecedenceBeforeEvaluator : PatternEvaluatorBase("src/test/resources/precedence-before/precedence-before.csv")

class PrecedenceAfterEvaluator : PatternEvaluatorBase("src/test/resources/precedence-after/precedence-after.csv")

class PrecedenceBetweenEvaluator : PatternEvaluatorBase("src/test/resources/precedence-between/precedence-between.csv")

class PrecedenceUntilEvaluator : PatternEvaluatorBase("src/test/resources/precedence-until/precedence-until.csv")


class PrecedenceConstraintGloballyEvaluator : PatternEvaluatorBase("src/test/resources/precedence-constraint-globally/precedence-constraint-globally.csv")

class PrecedenceConstraintBeforeEvaluator : PatternEvaluatorBase("src/test/resources/precedence-constraint-before/precedence-constraint-before.csv")

class PrecedenceConstraintAfterEvaluator : PatternEvaluatorBase("src/test/resources/precedence-constraint-after/precedence-constraint-after.csv")

class PrecedenceConstraintBetweenEvaluator : PatternEvaluatorBase("src/test/resources/precedence-constraint-between/precedence-constraint-between.csv")

class PrecedenceConstraintUntilEvaluator : PatternEvaluatorBase("src/test/resources/precedence-constraint-until/precedence-constraint-until.csv")

// Precedence N1 Chain

class PrecedenceN1GloballyEvaluator : PatternEvaluatorBase("src/test/resources/precedence-n1-globally/precedence-n1-globally.csv")

class PrecedenceN1BeforeEvaluator : PatternEvaluatorBase("src/test/resources/precedence-n1-before/precedence-n1-before.csv")

class PrecedenceN1AfterEvaluator : PatternEvaluatorBase("src/test/resources/precedence-n1-after/precedence-n1-after.csv")

class PrecedenceN1BetweenEvaluator : PatternEvaluatorBase("src/test/resources/precedence-n1-between/precedence-n1-between.csv")

class PrecedenceN1UntilEvaluator : PatternEvaluatorBase("src/test/resources/precedence-n1-until/precedence-n1-until.csv")


class PrecedenceN1ConstraintGloballyEvaluator : PatternEvaluatorBase("src/test/resources/precedence-n1-constraint-globally/precedence-n1-constraint-globally.csv")

class PrecedenceN1ConstraintBeforeEvaluator : PatternEvaluatorBase("src/test/resources/precedence-n1-constraint-before/precedence-n1-constraint-before.csv")

class PrecedenceN1ConstraintAfterEvaluator : PatternEvaluatorBase("src/test/resources/precedence-n1-constraint-after/precedence-n1-constraint-after.csv")

class PrecedenceN1ConstraintBetweenEvaluator : PatternEvaluatorBase("src/test/resources/precedence-n1-constraint-between/precedence-n1-constraint-between.csv")

class PrecedenceN1ConstraintUntilEvaluator : PatternEvaluatorBase("src/test/resources/precedence-n1-constraint-until/precedence-n1-constraint-until.csv")

// Relaxed Precedence N1 Chain

class RelaxedPrecedenceN1GloballyEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-precedence-n1-globally/relaxed-precedence-n1-globally.csv")

class RelaxedPrecedenceN1BeforeEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-precedence-n1-before/relaxed-precedence-n1-before.csv")

class RelaxedPrecedenceN1AfterEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-precedence-n1-after/relaxed-precedence-n1-after.csv")

class RelaxedPrecedenceN1BetweenEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-precedence-n1-between/relaxed-precedence-n1-between.csv")

class RelaxedPrecedenceN1UntilEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-precedence-n1-until/relaxed-precedence-n1-until.csv")


class RelaxedPrecedenceN1ConstraintGloballyEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-precedence-n1-constraint-globally/relaxed-precedence-n1-constraint-globally.csv")

class RelaxedPrecedenceN1ConstraintBeforeEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-precedence-n1-constraint-before/relaxed-precedence-n1-constraint-before.csv")

class RelaxedPrecedenceN1ConstraintAfterEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-precedence-n1-constraint-after/relaxed-precedence-n1-constraint-after.csv")

class RelaxedPrecedenceN1ConstraintBetweenEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-precedence-n1-constraint-between/relaxed-precedence-n1-constraint-between.csv")

class RelaxedPrecedenceN1ConstraintUntilEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-precedence-n1-constraint-until/relaxed-precedence-n1-constraint-until.csv")

// Precedence 1N Chain

class Precedence1NGloballyEvaluator : PatternEvaluatorBase("src/test/resources/precedence-1n-globally/precedence-1n-globally.csv")

class Precedence1NBeforeEvaluator : PatternEvaluatorBase("src/test/resources/precedence-1n-before/precedence-1n-before.csv")

class Precedence1NAfterEvaluator : PatternEvaluatorBase("src/test/resources/precedence-1n-after/precedence-1n-after.csv")

class Precedence1NBetweenEvaluator : PatternEvaluatorBase("src/test/resources/precedence-1n-between/precedence-1n-between.csv")

class Precedence1NUntilEvaluator : PatternEvaluatorBase("src/test/resources/precedence-1n-until/precedence-1n-until.csv")


class Precedence1NConstraintGloballyEvaluator : PatternEvaluatorBase("src/test/resources/precedence-1n-constraint-globally/precedence-1n-constraint-globally.csv")

class Precedence1NConstraintBeforeEvaluator : PatternEvaluatorBase("src/test/resources/precedence-1n-constraint-before/precedence-1n-constraint-before.csv")

class Precedence1NConstraintAfterEvaluator : PatternEvaluatorBase("src/test/resources/precedence-1n-constraint-after/precedence-1n-constraint-after.csv")

class Precedence1NConstraintBetweenEvaluator : PatternEvaluatorBase("src/test/resources/precedence-1n-constraint-between/precedence-1n-constraint-between.csv")

class Precedence1NConstraintUntilEvaluator : PatternEvaluatorBase("src/test/resources/precedence-1n-constraint-until/precedence-1n-constraint-until.csv")

// Relaxed Precedence 1N Chain

class RelaxedPrecedence1NGloballyEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-precedence-1n-globally/relaxed-precedence-1n-globally.csv")

class RelaxedPrecedence1NBeforeEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-precedence-1n-before/relaxed-precedence-1n-before.csv")

class RelaxedPrecedence1NAfterEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-precedence-1n-after/relaxed-precedence-1n-after.csv")

class RelaxedPrecedence1NBetweenEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-precedence-1n-between/relaxed-precedence-1n-between.csv")

class RelaxedPrecedence1NUntilEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-precedence-1n-until/relaxed-precedence-1n-until.csv")


class RelaxedPrecedence1NConstraintGloballyEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-precedence-1n-constraint-globally/relaxed-precedence-1n-constraint-globally.csv")

class RelaxedPrecedence1NConstraintBeforeEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-precedence-1n-constraint-before/relaxed-precedence-1n-constraint-before.csv")

class RelaxedPrecedence1NConstraintAfterEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-precedence-1n-constraint-after/relaxed-precedence-1n-constraint-after.csv")

class RelaxedPrecedence1NConstraintBetweenEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-precedence-1n-constraint-between/relaxed-precedence-1n-constraint-between.csv")

class RelaxedPrecedence1NConstraintUntilEvaluator : PatternEvaluatorBase("src/test/resources/relaxed-precedence-1n-constraint-until/relaxed-precedence-1n-constraint-until.csv")

// Response Invariance

class ResponseInvarianceGloballyEvaluator : PatternEvaluatorBase("src/test/resources/response-invariance-globally/response-invariance-globally.csv")

class ResponseInvarianceBeforeEvaluator : PatternEvaluatorBase("src/test/resources/response-invariance-before/response-invariance-before.csv")

class ResponseInvarianceAfterEvaluator : PatternEvaluatorBase("src/test/resources/response-invariance-after/response-invariance-after.csv")

class ResponseInvarianceBetweenEvaluator : PatternEvaluatorBase("src/test/resources/response-invariance-between/response-invariance-between.csv")

class ResponseInvarianceUntilEvaluator : PatternEvaluatorBase("src/test/resources/response-invariance-until/response-invariance-until.csv")

// Until

class UntilGloballyEvaluator : PatternEvaluatorBase("src/test/resources/until-globally/until-globally.csv")

class UntilBeforeEvaluator : PatternEvaluatorBase("src/test/resources/until-before/until-before.csv")

class UntilAfterEvaluator : PatternEvaluatorBase("src/test/resources/until-after/until-after.csv")

class UntilBetweenEvaluator : PatternEvaluatorBase("src/test/resources/until-between/until-between.csv")

class UntilUntilEvaluator : PatternEvaluatorBase("src/test/resources/until-until/until-until.csv")