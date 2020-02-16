import io.hkhc.gradle.allpublish.PublicationFactory2

plugins {
    `maven-publish`
    signing
    id("com.jfrog.bintray")
}

val pc = PublicationFactory2(this)

publishing {
    publications {
        publication = pc.createMavenPublication(this)
    }
    repositories {
        publicationFactory.createMavenRepository(this)
    }

}

signing {
    sign(publication)
}
