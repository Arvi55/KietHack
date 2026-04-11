<template>
  <div class="relative min-h-screen min-h-[100dvh] w-full overflow-x-hidden bg-forest text-sage font-sans antialiased">
    <!-- Full-viewport cover: stays edge-to-edge even when the URL bar resizes on mobile -->
    <div class="pointer-events-none fixed inset-0 z-0" aria-hidden="true">
      <div
        class="absolute inset-0 h-full min-h-[100dvh] w-full bg-cover bg-center bg-no-repeat"
        style="background-image: url('/hero-wellness.png')"
      />
      <div
        class="absolute inset-0 min-h-[100dvh] bg-gradient-to-b from-[#2a2420]/55 via-[#3d3530]/30 to-[#2c2622]/62"
      />
      <div
        class="absolute inset-0 min-h-[100dvh] bg-gradient-to-r from-[#2f2925]/50 via-transparent to-[#3a322d]/48"
      />
      <div
        class="absolute inset-0 min-h-[100dvh] bg-gradient-to-b from-black/32 via-transparent to-black/42"
      />
    </div>



    <section
      class="relative z-10 flex min-h-screen min-h-[100dvh] flex-col justify-center pt-24 pb-16 sm:pt-28 lg:pb-24"
    >
      <div
        class="mx-auto flex w-full max-w-7xl flex-1 flex-col gap-10 px-4 sm:px-6 lg:flex-row lg:items-center lg:gap-12 lg:px-8"
      >
        <div class="flex max-w-2xl flex-1 flex-col justify-center">
          <h1
            class="font-serif text-4xl font-semibold leading-tight tracking-tight text-sage drop-shadow-md sm:text-5xl lg:text-6xl xl:text-7xl"
            :class="heroReady ? 'animate-fade-up' : 'opacity-0 translate-y-5'"
          >
            Always Here. Your Companion for Peace.
          </h1>
          <p
            class="mt-6 max-w-xl text-base leading-relaxed text-sage/95 sm:text-lg lg:text-xl"
            :class="heroReady ? 'animate-fade-up [animation-delay:120ms]' : 'opacity-0 translate-y-5'"
          >
            When the world feels too loud, Emotion Buddy listens. A private, safe, and intelligent space
            to process your feelings and find balance, 24/7.
          </p>
          <div
            class="mt-10"
            :class="heroReady ? 'animate-fade-up [animation-delay:240ms]' : 'opacity-0 translate-y-5'"
          >
            <router-link
              to="/signup"
              class="inline-flex items-center justify-center rounded-full bg-lime px-8 py-4 text-base font-semibold text-cream shadow-lg shadow-black/30 transition hover:brightness-95 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-rose sm:px-10 sm:text-lg"
            >
              Start Your Calm Now
              <ArrowRight class="ml-2 h-5 w-5 sm:h-6 sm:w-6" aria-hidden="true" />
            </router-link>
          </div>
        </div>

        <div
          class="flex w-full flex-col gap-5 lg:max-w-sm lg:flex-none"
          :class="heroReady ? 'animate-fade-up [animation-delay:360ms]' : 'opacity-0 translate-y-5'"
        >
          <div
            class="rounded-2xl border border-[rgb(255_250_245/0.28)] bg-[rgb(248_242_235/0.14)] p-5 shadow-2xl shadow-black/35 backdrop-blur-xl sm:p-6 lg:animate-float"
          >
            <div class="mb-4 flex items-center justify-between">
              <span class="text-sm font-semibold uppercase tracking-wider text-sage/85">Mood</span>
              <LineChart class="h-5 w-5 text-rose" aria-hidden="true" />
            </div>
            <p class="mb-4 text-xs text-sage/70">Last 7 days</p>
            <div class="flex h-28 items-end justify-between gap-2 px-1">
              <div
                v-for="(h, i) in moodBars"
                :key="i"
                class="w-full max-w-[2rem] rounded-t-md bg-gradient-to-t from-moss/55 to-rose/95 transition-all"
                :style="{ height: `${h}%` }"
              />
            </div>
            <div class="mt-3 flex items-center gap-2 text-sm text-sage/90">
              <TrendingUp class="h-4 w-4 text-rose" aria-hidden="true" />
              <span>Steady upward calm</span>
            </div>
          </div>

          <div
            class="rounded-2xl border border-[rgb(255_250_245/0.28)] bg-[rgb(248_242_235/0.14)] p-5 shadow-2xl shadow-black/35 backdrop-blur-xl sm:p-6 lg:ml-4 lg:animate-float lg:[animation-delay:0.6s]"
          >
            <div class="mb-3 flex items-center gap-2">
              <Lightbulb class="h-5 w-5 text-rose" aria-hidden="true" />
              <span class="text-sm font-semibold uppercase tracking-wider text-sage/85">Daily tip</span>
            </div>
            <p class="font-serif text-lg leading-snug text-sage">
              Take three slow breaths before you check your phone—your nervous system will thank you.
            </p>
            <div class="mt-4 flex items-center gap-2 text-xs text-sage/65">
              <Sparkles class="h-3.5 w-3.5 text-moss" aria-hidden="true" />
              <span>Curated for balance</span>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { ArrowRight, LineChart, TrendingUp, Lightbulb, Sparkles } from 'lucide-vue-next'

const heroReady = ref(false)
const moodBars = [45, 62, 55, 70, 68, 82, 78]

onMounted(() => {
  requestAnimationFrame(() => {
    heroReady.value = true
  })
})

onUnmounted(() => {
  // Cleanup if needed
})
</script>
