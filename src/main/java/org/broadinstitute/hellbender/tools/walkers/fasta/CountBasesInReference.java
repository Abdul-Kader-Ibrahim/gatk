package org.broadinstitute.hellbender.tools.walkers.fasta;

import com.google.common.annotations.VisibleForTesting;
import org.broadinstitute.barclay.argparser.ArgumentCollection;
import org.broadinstitute.barclay.argparser.CommandLineProgramProperties;
import org.broadinstitute.barclay.help.DocumentedFeature;
import org.broadinstitute.hellbender.cmdline.argumentcollections.SimpleOutputCollection;
import org.broadinstitute.hellbender.engine.FeatureContext;
import org.broadinstitute.hellbender.engine.ReadsContext;
import org.broadinstitute.hellbender.engine.ReferenceContext;
import org.broadinstitute.hellbender.engine.ReferenceWalker;
import picard.cmdline.programgroups.ReferenceProgramGroup;

@DocumentedFeature
@CommandLineProgramProperties(
        oneLineSummary = "Count the numbers of each base in a reference file",
        summary = "Count the number of times each individual base occurs in a reference file.",
        programGroup = ReferenceProgramGroup.class
)
public class CountBasesInReference extends ReferenceWalker {
    @ArgumentCollection
    final public SimpleOutputCollection out = new SimpleOutputCollection();

    @VisibleForTesting
    final long[] baseCounts = new long[256];

    @Override
    public void apply(ReferenceContext referenceContext, ReadsContext read, FeatureContext featureContext) {
        baseCounts[referenceContext.getBase()]++;
    }

    @Override
    public Object onTraversalSuccess() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < baseCounts.length; i++) {
            final long count = baseCounts[i];
            if (count > 0) {
                sb.append((char) i).append(" : ").append(count).append("\n");
            }
        }
        out.writeToOutput(sb.toString().getBytes());
        System.out.print(sb.toString());

        return 0;
    }
}
