package edu.guym.aligner.edit;

import java.util.function.Function;

public interface EditVisitor<T, R> {

    R visit(EqualEdit<T> edit);

    R visit(InsertEdit<T> edit);

    R visit(DeleteEdit<T> edit);

    R visit(SubstituteEdit<T> edit);

    R visit(TransposeEdit<T> edit);

    static <T, R> EditVisitorBuilder<T, R> builder() {
        return new EditVisitorBuilder<>();
    }

    class EditVisitorBuilder<T, R> {

        private Function<Edit<T>, R> onEqual = (e) -> null;
        private Function<Edit<T>, R> onInsert = (e) -> null;
        private Function<Edit<T>, R> onDelete = (e) -> null;
        private Function<Edit<T>, R> onSubstitute = (e) -> null;
        private Function<Edit<T>, R> onTranspose = (e) -> null;

        EditVisitorBuilder() {
        }

        public EditVisitorBuilder<T, R> onEqual(Function<Edit<T>, R> visitor) {
            this.onEqual = visitor;
            return this;
        }

        public EditVisitorBuilder<T, R> onInsert(Function<Edit<T>, R> visitor) {
            this.onInsert = visitor;
            return this;
        }

        public EditVisitorBuilder<T, R> onDelete(Function<Edit<T>, R> visitor) {
            this.onDelete = visitor;
            return this;
        }

        public EditVisitorBuilder<T, R> onSubstitute(Function<Edit<T>, R> visitor) {
            this.onSubstitute = visitor;
            return this;
        }

        public EditVisitorBuilder<T, R> onTranspose(Function<Edit<T>, R> visitor) {
            this.onTranspose = visitor;
            return this;
        }

        public EditVisitor<T, R> build() {
            return new EditVisitor<>() {

                @Override
                public R visit(EqualEdit<T> edit) {
                    return onEqual.apply(edit);
                }

                @Override
                public R visit(InsertEdit<T> edit) {
                    return onInsert.apply(edit);
                }

                @Override
                public R visit(DeleteEdit<T> edit) {
                    return onDelete.apply(edit);
                }

                @Override
                public R visit(SubstituteEdit<T> edit) {
                    return onSubstitute.apply(edit);
                }

                @Override
                public R visit(TransposeEdit<T> edit) {
                    return onTranspose.apply(edit);
                }
            };
        }

    }
}
