package thredds.catalog2.straightimpl;

import org.junit.Test;
import thredds.catalog2.ThreddsMetadata;
import thredds.catalog2.builder.BuilderIssues;
import thredds.catalog2.builder.ThreddsMetadataBuilder;

import java.util.List;

import static org.junit.Assert.*;

/**
 * _MORE_
 *
 * @author edavis
 */
public class ThreddsMetadataBuilderTest {

  @Test
  public void checkEmptyThreddsMetadataBuilderAndBuild() {
    ThreddsMetadataBuilderImpl threddsMetadataBuilder = new ThreddsMetadataBuilderImpl();
    assertNotNull(threddsMetadataBuilder);
    assertTrue(threddsMetadataBuilder.isEmpty());
    BuilderIssues builderIssues = threddsMetadataBuilder.checkForIssues();
    assertTrue( builderIssues.isEmpty());
    ThreddsMetadata threddsMetadata = threddsMetadataBuilder.build();
    assertTrue( threddsMetadata.isEmpty());
  }

  @Test
  public void checkContentAndBuild() {
    ThreddsMetadataBuilderImpl threddsMetadataBuilder = new ThreddsMetadataBuilderImpl();

    ThreddsMetadataBuilder.DocumentationBuilder docBuilder = threddsMetadataBuilder.addDocumentation(null, "title", "http;//stuff/a.b");
    ThreddsMetadataBuilder.DocumentationBuilder docBuilder2 = threddsMetadataBuilder.addDocumentation(null, "some content");
                              threddsMetadataBuilder.
    BuilderIssues builderIssues = threddsMetadataBuilder.checkForIssues();
    assertTrue( builderIssues.isEmpty());
    ThreddsMetadata threddsMetadata = threddsMetadataBuilder.build();
    assertFalse( threddsMetadata.isEmpty() );

    List<ThreddsMetadata.Documentation> documentation = threddsMetadata.getDocumentation();
    assertEquals( 2, documentation.size() );
  }
}
