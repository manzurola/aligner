package edu.guym.aligner.metrics;

public interface CostMetric<T> {

    double getDelete(T source);

    double getInsert(T target);

    double getSubstitute(T source, T target);

    double getTranspose(T[] source, T[] target);
    
    static <T> CostMetric.Builder<T> builder() {
        return new Builder<>();
    }

   class Builder<T> {
        private DeleteCost<T> deleteCost = (s) -> 1.0;
        private InsertCost<T> insertCost = (t) -> 1.0;
        private SubstituteCost<T> substituteCost = (s, t) -> 1.0;
        private TransposeCost<T> transposeCost = (s, t) -> s.length - 1.0;

        public Builder() {
        }

        public Builder<T> setDeleteCost(DeleteCost<T> deleteCost) {
            this.deleteCost = deleteCost;
            return this;
        }

        public Builder<T> setInsertCost(InsertCost<T> insertCost) {
            this.insertCost = insertCost;
            return this;
        }

        public Builder<T> setSubstituteCost(SubstituteCost<T> substituteCost) {
            this.substituteCost = substituteCost;
            return this;
        }

        public Builder<T> setTransposeCost(TransposeCost<T> transposeCost) {
            this.transposeCost = transposeCost;
            return this;
        }

        public CostMetric<T> build() {
            return new Impl<>(deleteCost, insertCost, substituteCost, transposeCost);
        }
    }
}

class Impl<T> implements CostMetric<T> {
    private final DeleteCost<T> deleteCost;
    private final InsertCost<T> insertCost;
    private final SubstituteCost<T> substituteCost;
    private final TransposeCost<T> transposeCost;

    public Impl(DeleteCost<T> deleteCost,
                InsertCost<T> insertCost,
                SubstituteCost<T> substituteCost,
                TransposeCost<T> transposeCost) {
        this.deleteCost = deleteCost;
        this.insertCost = insertCost;
        this.substituteCost = substituteCost;
        this.transposeCost = transposeCost;
    }


    @Override
    public double getDelete(T source) {
        return deleteCost.getCost(source);
    }

    @Override
    public double getInsert(T target) {
        return insertCost.getCost(target);
    }

    @Override
    public double getSubstitute(T source, T target) {
        return substituteCost.getCost(source, target);
    }

    @Override
    public double getTranspose(T[] source, T[] target) {
        return transposeCost.getCost(source, target);
    }
}


