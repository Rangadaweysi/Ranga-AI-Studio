package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.CharacterEntity
import com.example.data.model.ProjectEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("RANGA AI STUDIO", appName)
  }

  @Test
  fun `verify project entity fields`() {
    val project = ProjectEntity(
      id = 1L,
      name = "Aventuras das Frutinhas",
      description = "A história de frutas que vivem grandes aventuras juntas.",
      type = "Novela",
      category = "Infantil / Animação 3D",
      coverUri = "cover_frutinhas",
      status = "Rascunho"
    )
    assertEquals("Aventuras das Frutinhas", project.name)
    assertEquals("Novela", project.type)
    assertEquals("Rascunho", project.status)
    assertTrue(project.updatedAt > 0)
  }

  @Test
  fun `verify character entity creation and properties`() {
    val character = CharacterEntity(
      id = 1L,
      projectId = 1L,
      name = "António",
      personality = "Alegre, brincalhão e muito curioso.",
      age = "-",
      description = "Maçã vermelha carismática",
      history = "Nasceu na grande macieira mágica",
      characterType = "Fruta",
      voice = "Voz Masculina 1",
      imageUri = "char_antonio"
    )
    assertEquals("António", character.name)
    assertEquals("Fruta", character.characterType)
    assertEquals("Voz Masculina 1", character.voice)
    assertEquals(1L, character.projectId)
    assertNotNull(character.imageUri)
  }
}
